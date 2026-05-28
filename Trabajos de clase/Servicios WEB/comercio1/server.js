const http = require('http');
const fs = require('fs');
const path = require('path');
const { URL } = require('url');

const PORT = Number(process.env.PORT || 4001);
const INTERMEDIADOR_URL = process.env.INTERMEDIADOR_URL || 'http://127.0.0.1:8000';
const PUBLIC_DIR = path.join(__dirname, 'public');

const MIME_TYPES = {
  '.html': 'text/html; charset=utf-8',
  '.js': 'application/javascript; charset=utf-8',
  '.css': 'text/css; charset=utf-8',
  '.json': 'application/json; charset=utf-8',
  '.svg': 'image/svg+xml',
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.jpeg': 'image/jpeg',
  '.ico': 'image/x-icon',
};

function sendJson(res, statusCode, payload) {
  res.writeHead(statusCode, {
    'Content-Type': 'application/json; charset=utf-8',
    'Access-Control-Allow-Origin': '*',
  });
  res.end(JSON.stringify(payload));
}

function sendText(res, statusCode, text, contentType = 'text/plain; charset=utf-8') {
  res.writeHead(statusCode, {
    'Content-Type': contentType,
    'Access-Control-Allow-Origin': '*',
  });
  res.end(text);
}

function serveStatic(req, res, pathname) {
  const safePath = pathname === '/' ? '/index.html' : pathname;
  const filePath = path.join(PUBLIC_DIR, safePath.replace(/^\/+/, ''));

  if (!filePath.startsWith(PUBLIC_DIR)) {
    sendText(res, 403, 'Forbidden');
    return;
  }

  fs.readFile(filePath, (error, data) => {
    if (error) {
      sendText(res, 404, 'Not found');
      return;
    }

    const ext = path.extname(filePath).toLowerCase();
    res.writeHead(200, {
      'Content-Type': MIME_TYPES[ext] || 'application/octet-stream',
      'Access-Control-Allow-Origin': '*',
    });
    res.end(data);
  });
}

async function readJsonBody(req) {
  const chunks = [];
  for await (const chunk of req) {
    chunks.push(chunk);
  }

  const raw = Buffer.concat(chunks).toString('utf8');
  if (!raw.trim()) {
    return {};
  }

  try {
    return JSON.parse(raw);
  } catch (error) {
    throw new Error('Invalid JSON body');
  }
}

async function proxyJson(req, res, targetPath) {
  try {
    const body = await readJsonBody(req);
    const targetUrl = new URL(targetPath, INTERMEDIADOR_URL);
    const headers = {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    };

    if (body && body.token) {
      headers.Authorization = `Bearer ${body.token}`;
      delete body.token;
    }

    const response = await fetch(targetUrl, {
      method: 'POST',
      headers,
      body: JSON.stringify(body),
    });

    const text = await response.text();
    res.writeHead(response.status, {
      'Content-Type': response.headers.get('content-type') || 'application/json; charset=utf-8',
      'Access-Control-Allow-Origin': '*',
    });
    res.end(text);
  } catch (error) {
    sendJson(res, 400, { error: 'proxy_error', message: error.message });
  }
}

const server = http.createServer((req, res) => {
  const url = new URL(req.url, `http://${req.headers.host}`);

  if (req.method === 'OPTIONS') {
    res.writeHead(204, {
      'Access-Control-Allow-Origin': '*',
      'Access-Control-Allow-Methods': 'GET,POST,OPTIONS',
      'Access-Control-Allow-Headers': 'Content-Type, Authorization',
    });
    res.end();
    return;
  }

  if (url.pathname === '/health') {
    sendJson(res, 200, {
      service: 'comercio1',
      intermediador: INTERMEDIADOR_URL,
      status: 'ok',
    });
    return;
  }

  if (url.pathname === '/api/login' && req.method === 'POST') {
    proxyJson(req, res, '/login');
    return;
  }

  if (url.pathname === '/api/transaccion' && req.method === 'POST') {
    proxyJson(req, res, '/transaccion');
    return;
  }

  if (req.method === 'GET') {
    serveStatic(req, res, url.pathname);
    return;
  }

  sendJson(res, 404, { error: 'not_found', message: 'Ruta no encontrada' });
});

server.listen(PORT, () => {
  console.log(`Comercio 1 running on http://127.0.0.1:${PORT}`);
  console.log(`Proxying to ${INTERMEDIADOR_URL}`);
});
