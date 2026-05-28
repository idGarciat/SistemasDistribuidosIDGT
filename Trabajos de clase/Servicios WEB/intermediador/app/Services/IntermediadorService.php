<?php

namespace App\Services;

use Illuminate\Http\Client\Response;
use Illuminate\Support\Facades\Http;
use Illuminate\Support\Str;
use RuntimeException;

class IntermediadorService
{
    public function issueToken(?string $usuario): array
    {
        $payload = [
            'sub' => $usuario ?: 'cliente',
            'iss' => config('intermediador.token_issuer'),
            'iat' => now()->timestamp,
            'exp' => now()->addMinutes((int) config('intermediador.token_ttl_minutes'))->timestamp,
        ];

        return [
            'token' => base64_encode(json_encode($payload, JSON_UNESCAPED_SLASHES)),
            'token_type' => 'Bearer',
            'expires_at' => now()->addMinutes((int) config('intermediador.token_ttl_minutes'))->toIso8601String(),
        ];
    }

    public function authorizeBearer(?string $authorizationHeader): bool
    {
        return is_string($authorizationHeader) && Str::startsWith($authorizationHeader, 'Bearer ');
    }

    public function registerTransaction(array $data): array
    {
        $sourceAccountId = (string) $data['cuentaOrigen'];
        $destinationAccountId = (string) $data['cuentaDestino'];
        $amount = (float) $data['monto'];

        $sourceLocation = $this->resolveAccountLocation($sourceAccountId);
        $destinationLocation = $this->resolveAccountLocation($destinationAccountId);

        if ($sourceLocation === null) {
            throw new RuntimeException('La cuenta origen no existe en BNB ni en Banco Económico.', 404);
        }

        if ($destinationLocation === null) {
            throw new RuntimeException('La cuenta destino no existe en BNB ni en Banco Económico.', 404);
        }

        $sourceBalance = $sourceLocation['bank'] === 'bco'
            ? $this->fetchBancoSaldoSoap($sourceAccountId)
            : (float) ($sourceLocation['account']['saldo'] ?? 0);

        if ($sourceBalance < $amount) {
            throw new RuntimeException('Saldo insuficiente en la cuenta origen.', 422);
        }

        $updatedSource = $this->updateAccountBalance($sourceLocation['bank'], $sourceAccountId, $sourceBalance - $amount);
        try {
            $destinationCurrentBalance = $destinationLocation['bank'] === 'bco'
                ? $this->fetchBancoSaldoSoap($destinationAccountId)
                : (float) ($destinationLocation['account']['saldo'] ?? 0);

            $updatedDestination = $this->updateAccountBalance($destinationLocation['bank'], $destinationAccountId, $destinationCurrentBalance + $amount);
        } catch (RuntimeException $exception) {
            $this->updateAccountBalance($sourceLocation['bank'], $sourceAccountId, $sourceBalance);
            throw $exception;
        }

        $movements = [];
        $movements[] = $this->registerAccountMovement($sourceLocation['bank'], $sourceAccountId, -$amount, 'Salida por transacción del intermediador');
        $movements[] = $this->registerAccountMovement($destinationLocation['bank'], $destinationAccountId, $amount, 'Entrada por transacción del intermediador');

        $transactionId = 'trx_' . now()->format('YmdHis') . '_' . Str::lower(Str::random(6));
        $result = [
            'transaction_id' => $transactionId,
            'status' => 'completed',
            'source' => [
                'name' => 'intermediador',
                'timestamp' => now()->toIso8601String(),
            ],
            'payload' => $data,
            'source_bank' => $sourceLocation['bank'],
            'destination_bank' => $destinationLocation['bank'],
            'updated_source' => $updatedSource,
            'updated_destination' => $updatedDestination,
            'movements' => $movements,
            'next_steps' => [
                'validar historial en Banco Económico si se requiere',
                'registrar operación final si se requiere',
            ],
        ];

        if ($sourceLocation['bank'] === 'bco' || $destinationLocation['bank'] === 'bco') {
            try {
                $result['banco_economico'] = $this->buildBancoEconomicoTrace(
                    $sourceAccountId,
                    $destinationAccountId,
                    $sourceLocation,
                    $destinationLocation
                );
            } catch (\Throwable $e) {
                $result['banco_economico'] = ['error' => $e->getMessage()];
            }
        }

        return $result;
    }

    private function resolveAccountLocation(string $accountId): ?array
    {
        $bnbAccount = $this->fetchBnbAccount($accountId);
        if ($bnbAccount !== null) {
            return [
                'bank' => 'bnb',
                'account' => $bnbAccount,
            ];
        }

        $bcoAccount = $this->fetchBancoAccountGraphQL($accountId);
        if ($bcoAccount !== null) {
            return [
                'bank' => 'bco',
                'account' => $bcoAccount,
            ];
        }

        return null;
    }

    private function fetchBnbAccount(string $accountId): ?array
    {
        $response = $this->bnbClient()
            ->get($this->bnbUrl('/cuenta/' . $accountId));

        if ($response->status() === 404) {
            return null;
        }

        $this->ensureSuccessfulResponse($response, 'No fue posible consultar la cuenta en BNB.');

        return $response->json('data');
    }

    private function updateAccountBalance(string $bank, string $accountId, float $saldo): array
    {
        if ($bank === 'bnb') {
            return $this->updateBnbBalance($accountId, $saldo);
        }

        if ($bank === 'bco') {
            return $this->updateBancoBalanceGraphQL($accountId, $saldo);
        }

        throw new RuntimeException('Banco no soportado para actualizar saldo.');
    }

    private function updateBnbBalance(string $accountId, float $saldo): array
    {
        $response = $this->bnbClient()
            ->patch($this->bnbUrl('/cuenta/' . $accountId . '/saldo'), [
                'saldo' => $saldo,
            ]);

        $this->ensureSuccessfulResponse($response, 'No fue posible actualizar el saldo en BNB.');

        return $response->json('data');
    }

    private function bnbClient()
    {
        return Http::acceptJson()->timeout(10)->retry(1, 100);
    }

    private function bcoClient()
    {
        return Http::acceptJson()->timeout(10)->retry(1, 100);
    }

    private function bnbUrl(string $path): string
    {
        $baseUrl = rtrim((string) config('intermediador.services.bnb.base_url'), '/');

        return $baseUrl . '/' . ltrim($path, '/');
    }

    private function bcoGraphqlEndpoint(): string
    {
        $base = rtrim((string) config('intermediador.services.banco_economico.rest_base_url'), '/');
        $path = ltrim((string) config('intermediador.services.banco_economico.graphql_path'), '/');

        return $base . '/' . $path;
    }

    private function bcoSoapEndpoint(): string
    {
        $base = rtrim((string) config('intermediador.services.banco_economico.rest_base_url'), '/');
        $path = ltrim((string) config('intermediador.services.banco_economico.soap_path'), '/');

        return $base . '/' . $path;
    }

    private function fetchBancoAccountGraphQL(string $accountId): ?array
    {
        $endpoint = $this->bcoGraphqlEndpoint();
        $query = <<<'GQL'
query Account($cuenta: String!) { account(cuenta: $cuenta) { cuenta ci nombres apellidos saldo } }
GQL;

        $response = $this->bcoClient()->post($endpoint, [
            'query' => $query,
            'variables' => ['cuenta' => $accountId],
        ]);

        if (! $response->successful()) {
            return null;
        }

        return $response->json('data.account');
    }

    private function fetchBancoSaldoSoap(string $accountId): float
    {
        $endpoint = $this->bcoSoapEndpoint();
        $soapBody = '<?xml version="1.0" encoding="UTF-8"?>' .
            '<consultarSaldo><cuenta>' . htmlspecialchars($accountId, ENT_XML1 | ENT_QUOTES, 'UTF-8') . '</cuenta></consultarSaldo>';

        $response = $this->bcoClient()->withHeaders([
            'Accept' => 'text/xml',
        ])->withBody($soapBody, 'text/xml')->post($endpoint);

        if (! $response->successful()) {
            throw new RuntimeException('Error consultando saldo en Banco Económico por SOAP: ' . $response->body(), $response->status());
        }

        if (! preg_match('/<saldo>([^<]+)<\/saldo>/i', $response->body(), $matches)) {
            throw new RuntimeException('No se pudo leer el saldo SOAP de Banco Económico.');
        }

        return (float) $matches[1];
    }

    private function updateBancoBalanceGraphQL(string $accountId, float $saldo): ?array
    {
        $endpoint = $this->bcoGraphqlEndpoint();
        $mutation = <<<'GQL'
mutation UpdateBalance($cuenta: String!, $saldo: Float!) { updateBalance(cuenta: $cuenta, saldo: $saldo) { cuenta ci nombres apellidos saldo } }
GQL;

        $response = $this->bcoClient()->post($endpoint, [
            'query' => $mutation,
            'variables' => ['cuenta' => $accountId, 'saldo' => $saldo],
        ]);

        if (! $response->successful()) {
            throw new RuntimeException('Error updating Banco Económico balance: ' . $response->body(), $response->status());
        }

        return $response->json('data.updateBalance');
    }

    private function registerBancoMovementGraphQL(string $accountId, float $amount, string $description): ?array
    {
        $endpoint = $this->bcoGraphqlEndpoint();
        $mutation = <<<'GQL'
mutation AddMovement($fecha: String!, $cuenta: String!, $monto: Float!, $descripcion: String) { addMovement(fecha: $fecha, cuenta: $cuenta, monto: $monto, descripcion: $descripcion) { id fecha cuenta monto descripcion } }
GQL;

        $response = $this->bcoClient()->post($endpoint, [
            'query' => $mutation,
            'variables' => [
                'fecha' => now()->toIso8601String(),
                'cuenta' => $accountId,
                'monto' => $amount,
                'descripcion' => $description,
            ],
        ]);

        if (! $response->successful()) {
            throw new RuntimeException('Error registering movement in Banco Económico: ' . $response->body(), $response->status());
        }

        return $response->json('data.addMovement');
    }

    private function registerAccountMovement(string $bank, string $accountId, float $amount, string $description): array
    {
        if ($bank === 'bnb') {
            return $this->registerBnbMovement($accountId, $amount, $description);
        }

        if ($bank === 'bco') {
            $movement = $this->registerBancoMovementGraphQL($accountId, $amount, $description);

            if ($movement === null) {
                throw new RuntimeException('No fue posible registrar el movimiento en Banco Económico.');
            }

            return $movement;
        }

        throw new RuntimeException('Banco no soportado para registrar movimiento.');
    }

    private function registerBnbMovement(string $accountId, float $amount, string $description): array
    {
        $response = $this->bnbClient()
            ->post($this->bnbUrl('/movimiento'), [
                'fecha' => now()->toIso8601String(),
                'cuenta' => $accountId,
                'monto' => $amount,
                'descripcion' => $description,
            ]);

        $this->ensureSuccessfulResponse($response, 'No fue posible registrar el movimiento en BNB.');

        return $response->json('data');
    }

    private function buildBancoEconomicoTrace(string $sourceAccountId, string $destinationAccountId, array $sourceLocation, array $destinationLocation): array
    {
        $trace = [];

        if (($sourceLocation['bank'] ?? null) === 'bco') {
            $trace['source_balance_soap'] = $this->fetchBancoSaldoSoap($sourceAccountId);
            $trace['source_account_graphql'] = $this->fetchBancoAccountGraphQL($sourceAccountId);
            $trace['history_source'] = $this->bcoSoapHistorial($sourceAccountId);
        }

        if (($destinationLocation['bank'] ?? null) === 'bco') {
            $trace['destination_balance_soap'] = $this->fetchBancoSaldoSoap($destinationAccountId);
            $trace['destination_account_graphql'] = $this->fetchBancoAccountGraphQL($destinationAccountId);
            $trace['history_destination'] = $this->bcoSoapHistorial($destinationAccountId);
        }

        return $trace;
    }

    private function bcoSoapHistorial(string $accountId): string
    {
        $endpoint = $this->bcoSoapEndpoint();
        $soapBody = '<?xml version="1.0" encoding="UTF-8"?>' .
            '<historial><cuenta>' . htmlspecialchars($accountId, ENT_XML1 | ENT_QUOTES, 'UTF-8') . '</cuenta></historial>';

        $response = $this->bcoClient()->withHeaders([
            'Accept' => 'text/xml',
        ])->withBody($soapBody, 'text/xml')->post($endpoint);

        if (! $response->successful()) {
            throw new RuntimeException('Error consultando historial en Banco Económico por SOAP: ' . $response->body(), $response->status());
        }

        return $response->body();
    }

    private function ensureSuccessfulResponse(Response $response, string $message): void
    {
        if ($response->successful()) {
            return;
        }

        $status = $response->status();
        $responseMessage = $response->json('message') ?? $response->body();

        throw new RuntimeException($message . ' ' . trim((string) $responseMessage), $status >= 400 ? $status : 500);
    }
}