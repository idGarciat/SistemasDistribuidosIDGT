const express = require('express');
const { graphqlHTTP } = require('express-graphql');
const fs = require('fs');
const path = require('path');
const schema = require('./schema');

const DATA_PATH = path.join(__dirname, 'data', 'accounts.json');
const PORT = process.env.PORT || 3002;

const app = express();
app.use(express.json());

app.get('/', function (req, res) {
  res.json({ service: 'Banco Economico', version: '1.0.0', graphql: '/graphql', soap: '/soap' });
});

app.use('/graphql', graphqlHTTP({
  schema: schema,
  graphiql: true,
}));

app.post('/soap', express.text({ type: '*/*' }), function (req, res) {
  const body = typeof req.body === 'string' ? req.body : '';
  const operation = detectSoapOperation(body, req.headers.soapaction);

  if (!operation) {
    res.status(400).type('text/xml').send(buildSoapFault('Unsupported operation', 'No se reconocio consultarSaldo ni historial.'));
    return;
  }

  const data = loadData();

  if (operation === 'consultarSaldo') {
    const accountId = extractXmlValue(body, 'cuenta') || extractXmlValue(body, 'account');
    const account = data.accounts.find(function (entry) {
      return entry.cuenta === accountId;
    });

    if (!account) {
      res.status(404).type('text/xml').send(buildSoapFault('Account not found', 'La cuenta ' + accountId + ' no existe.'));
      return;
    }

    res.type('text/xml').send(buildSoapResponse('consultarSaldoResponse', '<saldo>' + escapeXml(String(account.saldo)) + '</saldo>'));
    return;
  }

  if (operation === 'historial') {
    const accountId = extractXmlValue(body, 'cuenta') || extractXmlValue(body, 'account');
    const movements = data.movements.filter(function (movement) {
      return movement.cuenta === accountId;
    });

    const items = movements.map(function (movement) {
      return '<movimiento>' +
        '<id>' + escapeXml(movement.id || '') + '</id>' +
        '<fecha>' + escapeXml(movement.fecha || '') + '</fecha>' +
        '<cuenta>' + escapeXml(movement.cuenta || '') + '</cuenta>' +
        '<monto>' + escapeXml(String(movement.monto == null ? '' : movement.monto)) + '</monto>' +
        '<descripcion>' + escapeXml(movement.descripcion || '') + '</descripcion>' +
      '</movimiento>';
    }).join('');

    res.type('text/xml').send(buildSoapResponse('historialResponse', '<movimientos>' + items + '</movimientos>'));
    return;
  }

  res.status(400).type('text/xml').send(buildSoapFault('Unsupported operation', 'Solo se soportan consultarSaldo e historial.'));
});

app.listen(PORT, function () {
  console.log('Banco Economico GraphQL+SOAP running on http://127.0.0.1:' + PORT);
});

function loadData() {
  try {
    return JSON.parse(fs.readFileSync(DATA_PATH, 'utf8'));
  } catch (error) {
    return { accounts: [], movements: [] };
  }
}

function detectSoapOperation(body, soapActionHeader) {
  const soapAction = typeof soapActionHeader === 'string' ? soapActionHeader.replace(/"/g, '') : '';

  if (soapAction.indexOf('consultarSaldo') !== -1) {
    return 'consultarSaldo';
  }

  if (soapAction.indexOf('historial') !== -1) {
    return 'historial';
  }

  if (body.indexOf('<consultarSaldo') !== -1) {
    return 'consultarSaldo';
  }

  if (body.indexOf('<historial') !== -1) {
    return 'historial';
  }

  return null;
}

function extractXmlValue(xml, tagName) {
  const openTag = '<' + tagName + '>';
  const closeTag = '</' + tagName + '>';
  const start = xml.indexOf(openTag);
  const end = xml.indexOf(closeTag);

  if (start === -1 || end === -1 || end <= start) {
    return '';
  }

  return xml.substring(start + openTag.length, end).trim();
}

function escapeXml(value) {
  return String(value)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&apos;');
}

function buildSoapResponse(operationName, innerXml) {
  return '<?xml version="1.0" encoding="UTF-8"?>\n' +
    '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">\n' +
    '  <soap:Body>\n' +
    '    <' + operationName + '>' + innerXml + '</' + operationName + '>\n' +
    '  </soap:Body>\n' +
    '</soap:Envelope>';
}

function buildSoapFault(faultCode, faultString) {
  return '<?xml version="1.0" encoding="UTF-8"?>\n' +
    '<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">\n' +
    '  <soap:Body>\n' +
    '    <soap:Fault>\n' +
    '      <faultcode>' + escapeXml(faultCode) + '</faultcode>\n' +
    '      <faultstring>' + escapeXml(faultString) + '</faultstring>\n' +
    '    </soap:Fault>\n' +
    '  </soap:Body>\n' +
    '</soap:Envelope>';
}
