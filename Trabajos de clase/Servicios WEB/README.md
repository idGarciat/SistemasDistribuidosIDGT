Esqueleto del sistema de pasarela de pagos

Estructura de servicios:
- intermediador: pasarela central (endpoints /login, /transaccion)
- bnb: servicio REST en PHP puro que expone `/cuenta` y usa cURL para notificaciones opcionales
- banco_economico: servicio con REST/GraphQL/SOAP (stubs)
- comercio1: cliente web (HTML/JS)
- comercio2: cliente desktop (C# WinForms) - ejemplo de consumo
- common: modelos y datos compartidos

Instrucciones rápidas:
- Cada servicio tiene un servidor Node.js ligero listo para ejecutarse con `node <file>`.
- BNB se ejecuta con `php -S 127.0.0.1:3001 router.php` dentro de `bnb`.
- Puertos por defecto:
  - intermediador: 3000
  - bnb: 3001
  - banco_economico: 3002
- Para probar: abrir `comercio1/index.html` en el navegador y usar los botones de prueba.
