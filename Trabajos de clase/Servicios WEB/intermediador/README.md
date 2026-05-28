# Intermediador

Proyecto Laravel base para la pasarela de pagos.

## Diseño actual
- Controlador HTTP en `app/Http/Controllers/IntermediadorController.php`.
- Lógica de negocio separada en `app/Services/IntermediadorService.php`.
- Configuración de bancos y tiempos de token en `config/intermediador.php`.
- El intermediador consulta y actualiza BNB antes de aceptar la transacción.
- La transacción queda `completed` solo si BNB responde correctamente y los saldos se actualizan.

## Rutas
- `POST /login`
- `POST /transaccion`

## Variables de entorno
- `INTERMEDIADOR_TOKEN_ISSUER`
- `INTERMEDIADOR_TOKEN_TTL_MINUTES`
- `BNB_BASE_URL`
- `BANCO_ECONOMICO_REST_URL`
- `BANCO_ECONOMICO_GRAPHQL_PATH`
- `BANCO_ECONOMICO_SOAP_PATH`

## Ejecutar
```powershell
cd intermediador
php artisan serve
```

## Probar
```powershell
Invoke-WebRequest -Method Post http://127.0.0.1:8000/login
```

Ejemplo de transacción:
```powershell
Invoke-WebRequest -Method Post http://127.0.0.1:8000/transaccion -Headers @{Authorization='Bearer TOKEN'} -ContentType 'application/json' -Body '{"fecha":"2026-05-27T12:00:00Z","cuentaOrigen":"1001","cuentaDestino":"2001","monto":50}'
```

## Flujo real actual
1. `login` devuelve un token simulado tipo Bearer.
2. `transaccion` valida el token.
3. El intermediador consulta las cuentas en BNB.
4. Si hay saldo suficiente, descuenta y acredita el monto.
5. Registra movimientos en BNB y devuelve la respuesta final.
