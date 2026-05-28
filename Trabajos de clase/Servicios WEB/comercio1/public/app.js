const output = document.getElementById('output');
const tokenPreview = document.getElementById('token-preview');
const btnLogin = document.getElementById('btn-login');
const btnTransfer = document.getElementById('btn-transfer');
const btnClear = document.getElementById('btn-clear');
const fechaInput = document.getElementById('fecha');

const tokenKey = 'comercio1_token';
let token = localStorage.getItem(tokenKey) || '';

function setOutput(value) {
  if (typeof value === 'string') {
    output.textContent = value;
    return;
  }

  output.textContent = JSON.stringify(value, null, 2);
}

function updateTokenPreview() {
  tokenPreview.textContent = token ? `${token.slice(0, 28)}...` : 'sin token';
}

function getIsoNowLocal() {
  const now = new Date();
  const offset = now.getTimezoneOffset();
  const local = new Date(now.getTime() - offset * 60000);
  return local.toISOString().slice(0, 16);
}

function readFormValue(id) {
  return document.getElementById(id).value.trim();
}

async function postJson(url, payload) {
  const response = await fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json',
    },
    body: JSON.stringify(payload),
  });

  const text = await response.text();
  let data;
  try {
    data = text ? JSON.parse(text) : {};
  } catch (error) {
    data = { raw: text };
  }

  if (!response.ok) {
    const message = data.message || data.error || 'Error inesperado';
    throw new Error(message);
  }

  return data;
}

btnLogin.addEventListener('click', async () => {
  try {
    setOutput('Autenticando...');
    const data = await postJson('/api/login', {
      usuario: readFormValue('usuario'),
      password: readFormValue('password'),
    });

    token = data.token;
    localStorage.setItem(tokenKey, token);
    updateTokenPreview();
    setOutput({ message: 'Login correcto', ...data });
  } catch (error) {
    setOutput({ error: error.message });
  }
});

btnTransfer.addEventListener('click', async () => {
  try {
    if (!token) {
      throw new Error('Primero debes iniciar sesión');
    }

    const fecha = readFormValue('fecha') || getIsoNowLocal();
    setOutput('Enviando transacción...');

    const data = await postJson('/api/transaccion', {
      token,
      fecha: new Date(fecha).toISOString(),
      cuentaOrigen: readFormValue('cuentaOrigen'),
      cuentaDestino: readFormValue('cuentaDestino'),
      monto: Number(readFormValue('monto')),
    });

    setOutput(data);
  } catch (error) {
    setOutput({ error: error.message });
  }
});

btnClear.addEventListener('click', () => {
  output.textContent = 'Listo para operar.';
});

fechaInput.value = getIsoNowLocal();
updateTokenPreview();
if (token) {
  setOutput({ message: 'Token recuperado desde localStorage' });
}
