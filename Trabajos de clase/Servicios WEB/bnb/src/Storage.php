<?php

declare(strict_types=1);

namespace Bnb;

class Storage
{
    private string $filePath;

    public function __construct(string $filePath)
    {
        $this->filePath = $filePath;

        if (!file_exists($this->filePath)) {
            $this->seed();
        }
    }

    public function all(): array
    {
        return $this->load()['accounts'];
    }

    public function find(string $cuenta): ?array
    {
        foreach ($this->all() as $account) {
            if (($account['cuenta'] ?? null) === $cuenta) {
                return $account;
            }
        }

        return null;
    }

    public function create(array $account): array
    {
        $data = $this->load();

        if ($this->find($account['cuenta']) !== null) {
            throw new \RuntimeException('La cuenta ya existe.');
        }

        $data['accounts'][] = $account;
        $this->persist($data);

        return $account;
    }

    public function updateBalance(string $cuenta, float $saldo): ?array
    {
        $data = $this->load();
        foreach ($data['accounts'] as $index => $account) {
            if (($account['cuenta'] ?? null) === $cuenta) {
                $data['accounts'][$index]['saldo'] = $saldo;
                $this->persist($data);

                return $data['accounts'][$index];
            }
        }

        return null;
    }

    public function addMovement(array $movement): array
    {
        $data = $this->load();
        $movement['id'] = 'mov_' . date('YmdHis') . '_' . bin2hex(random_bytes(3));
        $data['movements'][] = $movement;
        $this->persist($data);

        return $movement;
    }

    private function load(): array
    {
        $content = file_get_contents($this->filePath);
        if ($content === false || trim($content) === '') {
            return ['accounts' => [], 'movements' => []];
        }

        $decoded = json_decode($content, true);
        if (!is_array($decoded)) {
            return ['accounts' => [], 'movements' => []];
        }

        $decoded['accounts'] = $decoded['accounts'] ?? [];
        $decoded['movements'] = $decoded['movements'] ?? [];

        return $decoded;
    }

    private function persist(array $data): void
    {
        file_put_contents(
            $this->filePath,
            json_encode($data, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES)
        );
    }

    private function seed(): void
    {
        $seed = [
            'accounts' => [
                [
                    'cuenta' => '1001',
                    'ci' => '123456',
                    'nombres' => 'Juan',
                    'apellidos' => 'Perez',
                    'saldo' => 1000.0,
                ],
                [
                    'cuenta' => '1002',
                    'ci' => '234567',
                    'nombres' => 'Ana',
                    'apellidos' => 'Lopez',
                    'saldo' => 500.0,
                ],
            ],
            'movements' => [],
        ];

        file_put_contents(
            $this->filePath,
            json_encode($seed, JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES)
        );
    }
}
