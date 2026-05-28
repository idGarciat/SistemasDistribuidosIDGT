<?php

declare(strict_types=1);

require __DIR__ . '/src/Storage.php';
require __DIR__ . '/src/CurlClient.php';

use Bnb\Storage;
use Bnb\CurlClient;

header('Content-Type: application/json; charset=utf-8');

$storage = new Storage(__DIR__ . '/data/accounts.json');
$curlClient = new CurlClient();

$method = $_SERVER['REQUEST_METHOD'] ?? 'GET';
$path = parse_url($_SERVER['REQUEST_URI'] ?? '/', PHP_URL_PATH) ?: '/';
$segments = array_values(array_filter(explode('/', trim($path, '/'))));

function jsonResponse(int $statusCode, array $payload): void
{
    http_response_code($statusCode);
    echo json_encode($payload, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
    exit;
}

function readJsonBody(): array
{
    $raw = file_get_contents('php://input');
    if ($raw === false || trim($raw) === '') {
        return [];
    }

    $decoded = json_decode($raw, true);
    if (!is_array($decoded)) {
        jsonResponse(400, [
            'error' => 'invalid_json',
            'message' => 'El cuerpo debe ser JSON válido.',
        ]);
    }

    return $decoded;
}

function findAccountIndex(array $accounts, string $cuenta): ?int
{
    foreach ($accounts as $index => $account) {
        if (($account['cuenta'] ?? null) === $cuenta) {
            return $index;
        }
    }

    return null;
}

if ($path === '/' && $method === 'GET') {
    jsonResponse(200, [
        'service' => 'BNB',
        'version' => '1.0.0',
        'resources' => [
            'GET /cuenta',
            'GET /cuenta/{id}',
            'POST /cuenta',
            'PATCH /cuenta/{id}/saldo',
            'POST /movimiento',
        ],
    ]);
}

if ($segments === ['cuenta'] && $method === 'GET') {
    jsonResponse(200, [
        'data' => $storage->all(),
    ]);
}

if (count($segments) === 2 && $segments[0] === 'cuenta' && $method === 'GET') {
    $account = $storage->find($segments[1]);
    if ($account === null) {
        jsonResponse(404, [
            'error' => 'not_found',
            'message' => 'La cuenta no existe.',
        ]);
    }

    jsonResponse(200, [
        'data' => $account,
    ]);
}

if ($segments === ['cuenta'] && $method === 'POST') {
    $body = readJsonBody();
    $required = ['cuenta', 'ci', 'nombres', 'apellidos', 'saldo'];
    foreach ($required as $field) {
        if (!array_key_exists($field, $body)) {
            jsonResponse(422, [
                'error' => 'validation_error',
                'message' => "Falta el campo {$field}.",
            ]);
        }
    }

    if (!is_numeric($body['saldo'])) {
        jsonResponse(422, [
            'error' => 'validation_error',
            'message' => 'El saldo debe ser numérico.',
        ]);
    }

    $created = $storage->create([
        'cuenta' => (string) $body['cuenta'],
        'ci' => (string) $body['ci'],
        'nombres' => (string) $body['nombres'],
        'apellidos' => (string) $body['apellidos'],
        'saldo' => (float) $body['saldo'],
    ]);

    jsonResponse(201, [
        'message' => 'Cuenta creada.',
        'data' => $created,
    ]);
}

if (count($segments) === 3 && $segments[0] === 'cuenta' && $segments[2] === 'saldo' && $method === 'PATCH') {
    $accountId = $segments[1];
    $body = readJsonBody();
    if (!isset($body['saldo']) || !is_numeric($body['saldo'])) {
        jsonResponse(422, [
            'error' => 'validation_error',
            'message' => 'El campo saldo es obligatorio y debe ser numérico.',
        ]);
    }

    $updated = $storage->updateBalance($accountId, (float) $body['saldo']);
    if ($updated === null) {
        jsonResponse(404, [
            'error' => 'not_found',
            'message' => 'La cuenta no existe.',
        ]);
    }

    $webhookUrl = getenv('BNB_AUDIT_WEBHOOK_URL') ?: '';
    if ($webhookUrl !== '') {
        $curlClient->postJson($webhookUrl, [
            'event' => 'balance_updated',
            'account' => $updated,
        ]);
    }

    jsonResponse(200, [
        'message' => 'Saldo actualizado.',
        'data' => $updated,
    ]);
}

if ($segments === ['movimiento'] && $method === 'POST') {
    $body = readJsonBody();
    $required = ['fecha', 'cuenta', 'monto'];
    foreach ($required as $field) {
        if (!array_key_exists($field, $body)) {
            jsonResponse(422, [
                'error' => 'validation_error',
                'message' => "Falta el campo {$field}.",
            ]);
        }
    }

    if (!is_numeric($body['monto'])) {
        jsonResponse(422, [
            'error' => 'validation_error',
            'message' => 'El monto debe ser numérico.',
        ]);
    }

    $movement = $storage->addMovement([
        'fecha' => (string) $body['fecha'],
        'cuenta' => (string) $body['cuenta'],
        'monto' => (float) $body['monto'],
        'descripcion' => isset($body['descripcion']) ? (string) $body['descripcion'] : null,
    ]);

    jsonResponse(201, [
        'message' => 'Movimiento registrado.',
        'data' => $movement,
    ]);
}

jsonResponse(404, [
    'error' => 'not_found',
    'message' => 'Ruta no encontrada.',
]);
