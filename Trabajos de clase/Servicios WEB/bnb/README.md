# BNB

Servicio REST en PHP puro para administrar cuentas y saldos.

## Qué usa
- PHP nativo para las rutas y la API REST.
- cURL para notificaciones opcionales hacia un webhook externo cuando se actualiza un saldo.

## Endpoints
- `GET /` -> estado del servicio
- `GET /cuenta` -> lista cuentas
- `GET /cuenta/{id}` -> consulta una cuenta
- `POST /cuenta` -> crea una cuenta
- `PATCH /cuenta/{id}/saldo` -> actualiza saldo
- `POST /movimiento` -> registra un movimiento

## Ejecución
```powershell
cd bnb
php -S 127.0.0.1:3001 router.php
```

## Ejemplos
Listar cuentas:
```powershell
curl http://127.0.0.1:3001/cuenta
```

Actualizar saldo:
```powershell
curl -X PATCH http://127.0.0.1:3001/cuenta/1001/saldo -H "Content-Type: application/json" -d "{\"saldo\":950}"
```

Webhook opcional:
- Define `BNB_AUDIT_WEBHOOK_URL` para que el servicio use cURL y notifique cada cambio de saldo.
