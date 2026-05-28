Banco Económico - GraphQL stub

Este servicio expone un endpoint GraphQL con las siguientes operaciones:

Queries:
- `accounts`: lista todas las cuentas
- `account(cuenta: String!)`: obtiene una cuenta por id
- `movements`: lista movimientos

Mutations:
- `updateBalance(cuenta: String!, saldo: Float!)` -> actualiza saldo
- `addMovement(fecha: String!, cuenta: String!, monto: Float!, descripcion: String)` -> registra movimiento
- `createAccount(...)` -> crea cuenta

Instalación y ejecución:

```powershell
cd "E:/Montis/SistemasDistribuidosIDGT/Trabajos de clase/Servicios WEB/banco_economico"
npm install
npm start
```

Por defecto corre en `http://127.0.0.1:3002/graphql` y es compatible con `intermediador` (configurar `BANCO_ECONOMICO_REST_URL` si se quisiera usar REST; actualmente es GraphQL).
