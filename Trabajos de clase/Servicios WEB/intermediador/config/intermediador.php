<?php

return [
    'token_issuer' => env('INTERMEDIADOR_TOKEN_ISSUER', 'intermediador'),
    'token_ttl_minutes' => (int) env('INTERMEDIADOR_TOKEN_TTL_MINUTES', 60),

    'services' => [
        'bnb' => [
            'base_url' => env('BNB_BASE_URL', 'http://127.0.0.1:3001'),
            'cuenta_path' => env('BNB_CUENTA_PATH', '/cuenta'),
        ],
        'banco_economico' => [
            'rest_base_url' => env('BANCO_ECONOMICO_REST_URL', 'http://127.0.0.1:3002'),
            'graphql_path' => env('BANCO_ECONOMICO_GRAPHQL_PATH', '/graphql'),
            'soap_path' => env('BANCO_ECONOMICO_SOAP_PATH', '/soap'),
        ],
    ],
];