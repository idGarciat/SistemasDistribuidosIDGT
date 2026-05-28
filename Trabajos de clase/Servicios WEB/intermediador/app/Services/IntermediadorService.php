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

        $sourceAccount = $this->fetchBnbAccount($sourceAccountId);
        $destinationAccount = $this->fetchBnbAccount($destinationAccountId);

        if ($sourceAccount === null) {
            throw new RuntimeException('La cuenta origen no existe en BNB.', 404);
        }

        if ($destinationAccount === null) {
            throw new RuntimeException('La cuenta destino no existe en BNB.', 404);
        }

        if ((float) ($sourceAccount['saldo'] ?? 0) < $amount) {
            throw new RuntimeException('Saldo insuficiente en la cuenta origen.', 422);
        }

        $updatedSource = $this->updateBnbBalance($sourceAccountId, (float) $sourceAccount['saldo'] - $amount);
        try {
            $updatedDestination = $this->updateBnbBalance($destinationAccountId, (float) $destinationAccount['saldo'] + $amount);
        } catch (RuntimeException $exception) {
            $this->updateBnbBalance($sourceAccountId, (float) $sourceAccount['saldo']);
            throw $exception;
        }

        $movements = [];
        $movements[] = $this->registerBnbMovement($sourceAccountId, -$amount, 'Salida por transacción del intermediador');
        $movements[] = $this->registerBnbMovement($destinationAccountId, $amount, 'Entrada por transacción del intermediador');

        $transactionId = 'trx_' . now()->format('YmdHis') . '_' . Str::lower(Str::random(6));
        $result = [
            'transaction_id' => $transactionId,
            'status' => 'completed',
            'source' => [
                'name' => 'intermediador',
                'timestamp' => now()->toIso8601String(),
            ],
            'payload' => $data,
            'bnb' => [
                'source_account' => $updatedSource,
                'destination_account' => $updatedDestination,
                'movements' => $movements,
            ],
            'next_steps' => [
                'validar saldo en Banco Económico',
                'registrar operación final si se requiere',
            ],
        ];

        // Intent: validate and mirror changes in Banco Económico via GraphQL (best-effort)
        try {
            $bcoSource = $this->fetchBancoAccountGraphQL($sourceAccountId);
            $bcoDestination = $this->fetchBancoAccountGraphQL($destinationAccountId);

            if ($bcoSource !== null && $bcoDestination !== null) {
                $bcoUpdatedSource = $this->updateBancoBalanceGraphQL($sourceAccountId, (float) $updatedSource['saldo']);
                $bcoUpdatedDestination = $this->updateBancoBalanceGraphQL($destinationAccountId, (float) $updatedDestination['saldo']);

                $bcoMovements = [];
                $bcoMovements[] = $this->registerBancoMovementGraphQL($sourceAccountId, -$amount, 'Salida por transacción del intermediador');
                $bcoMovements[] = $this->registerBancoMovementGraphQL($destinationAccountId, $amount, 'Entrada por transacción del intermediador');

                $result['banco_economico'] = [
                    'source_account' => $bcoUpdatedSource,
                    'destination_account' => $bcoUpdatedDestination,
                    'movements' => $bcoMovements,
                ];
            } else {
                $result['banco_economico'] = ['warning' => 'Alguna cuenta no existe en Banco Económico'];
            }
        } catch (Throwable $e) {
            $result['banco_economico'] = ['error' => $e->getMessage()];
        }

        return $result;
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

    private function updateBnbBalance(string $accountId, float $saldo): array
    {
        $response = $this->bnbClient()
            ->patch($this->bnbUrl('/cuenta/' . $accountId . '/saldo'), [
                'saldo' => $saldo,
            ]);

        $this->ensureSuccessfulResponse($response, 'No fue posible actualizar el saldo en BNB.');

        return $response->json('data');
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