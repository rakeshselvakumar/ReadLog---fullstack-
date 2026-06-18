// ==============================
// CONFIG — your backend URL
// ==============================

// Change this to your Render.com URL when deployed
const API_URL = 'http://localhost:8081';


// ==============================
// DARK MODE
// ==============================

const savedTheme = localStorage.getItem('theme') || 'light';
document.documentElement.setAttribute('data-theme', savedTheme);

function toggleDarkMode() {
  const current = document.documentElement.getAttribute('data-theme');
  const next    = current === 'dark' ? 'light' : 'dark';
  document.documentElement.setAttribute('data-theme', next);
  localStorage.setItem('theme', next);
}


// ==============================
// REDIRECT IF ALREADY LOGGED IN
// ==============================

// If user already has a token, skip login page
const existingToken = localStorage.getItem('token');
if (existingToken) {
  window.location.href = 'index.html';
}


// ==============================
// TAB SWITCHING
// ==============================

function switchTab(tab) {
  document.getElementById('signinForm').style.display   = 'none';
  document.getElementById('registerForm').style.display = 'none';
  document.getElementById('forgotForm').style.display   = 'none';

  document.getElementById('signinTab').classList.remove('active');
  document.getElementById('registerTab').classList.remove('active');

  if (tab === 'signin') {
    document.getElementById('signinForm').style.display = 'flex';
    document.getElementById('signinTab').classList.add('active');
  } else if (tab === 'register') {
    document.getElementById('registerForm').style.display = 'flex';
    document.getElementById('registerTab').classList.add('active');
  } else if (tab === 'forgot') {
    document.getElementById('forgotForm').style.display = 'flex';
  }

  clearAllErrors();
}

function showForgot() { switchTab('forgot'); }


// ==============================
// CLEAR ALL ERRORS
// ==============================

function clearAllErrors() {
  const ids = [
    'signinEmailErr', 'signinPasswordErr', 'signinBanner', 'signinSuccess',
    'regNameErr', 'regEmailErr', 'regPasswordErr', 'regConfirmErr',
    'registerBanner', 'registerSuccess', 'forgotEmailErr', 'forgotSuccess'
  ];
  ids.forEach(id => {
    const el = document.getElementById(id);
    if (el) { el.textContent = ''; el.style.display = 'none'; }
  });
  document.querySelectorAll('.field-wrap').forEach(w => w.classList.remove('error-field'));
}


// ==============================
// SHOW / HIDE PASSWORD
// ==============================

function togglePassword(inputId, btn) {
  const input = document.getElementById(inputId);
  if (input.type === 'password') {
    input.type = 'text';
    btn.textContent = '🙈';
  } else {
    input.type = 'password';
    btn.textContent = '👁';
  }
}


// ==============================
// PASSWORD STRENGTH
// ==============================

function checkStrength(password) {
  const segs  = ['seg1','seg2','seg3','seg4'];
  const label = document.getElementById('strengthLabel');

  segs.forEach(id => {
    document.getElementById(id).className = 'strength-seg';
  });

  if (!password) { label.textContent = ''; return; }

  let score = 0;
  if (password.length >= 8)           score++;
  if (/[A-Z]/.test(password))         score++;
  if (/[0-9]/.test(password))         score++;
  if (/[^A-Za-z0-9]/.test(password)) score++;

  const levels = {
    1: { cls:'weak',   text:'Weak',   color:'#e74c3c' },
    2: { cls:'ok',     text:'Fair',   color:'#f39c12' },
    3: { cls:'strong', text:'Good',   color:'#27ae60' },
    4: { cls:'strong', text:'Strong', color:'#27ae60' },
  };

  const level = levels[score] || levels[1];
  for (let i = 0; i < score; i++) {
    document.getElementById(segs[i]).classList.add(level.cls);
  }

  label.textContent = level.text;
  label.style.color = level.color;
}


// ==============================
// VALIDATION HELPERS
// ==============================

function showFieldError(inputId, errorId, message) {
  const input = document.getElementById(inputId);
  const err   = document.getElementById(errorId);
  if (input) input.closest('.field-wrap').classList.add('error-field');
  if (err)   { err.textContent = message; err.style.display = 'block'; }
}

function isValidEmail(email) {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

function showBanner(id, message, isError = true) {
  const el = document.getElementById(id);
  el.textContent    = message;
  el.style.display  = 'block';
}


// ==============================
// LOADING STATE
// ==============================

function setLoading(btnTextId, spinnerId, loading) {
  const btnText = document.getElementById(btnTextId);
  const spinner = document.getElementById(spinnerId);
  const btn     = btnText.closest('button');
  btnText.style.display = loading ? 'none'   : 'inline';
  spinner.style.display = loading ? 'block'  : 'none';
  btn.disabled          = loading;
}


// ==============================
// SIGN IN — calls real API
// ==============================

async function handleSignin() {
  clearAllErrors();

  const email    = document.getElementById('signinEmail').value.trim();
  const password = document.getElementById('signinPassword').value;

  let valid = true;

  if (!email) {
    showFieldError('signinEmail', 'signinEmailErr', 'Email is required');
    valid = false;
  } else if (!isValidEmail(email)) {
    showFieldError('signinEmail', 'signinEmailErr', 'Enter a valid email');
    valid = false;
  }

  if (!password) {
    showFieldError('signinPassword', 'signinPasswordErr', 'Password is required');
    valid = false;
  }

  if (!valid) return;

  setLoading('signinBtnText', 'signinSpinner', true);

  try {
    const response = await fetch(`${API_URL}/api/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    });

    const data = await response.json();

    if (!response.ok || !data.token) {
      // Show error from backend
      showBanner('signinBanner', data.message || 'Login failed. Please try again.');
    } else {
      // Save token and user info
      localStorage.setItem('token',    data.token);
      localStorage.setItem('userName', data.name);
      localStorage.setItem('userEmail', data.email);

      // Show success then redirect
      showBanner('signinSuccess', `Welcome back, ${data.name}! Redirecting...`);
      setTimeout(() => {
        window.location.href = 'index.html';
      }, 1000);
    }

  } catch (err) {
    // Network error — backend not running etc
    showBanner('signinBanner', '❌ Cannot connect to server. Make sure backend is running.');
  }

  setLoading('signinBtnText', 'signinSpinner', false);
}


// ==============================
// REGISTER — calls real API
// ==============================

async function handleRegister() {
  clearAllErrors();

  const name     = document.getElementById('regName').value.trim();
  const email    = document.getElementById('regEmail').value.trim();
  const password = document.getElementById('regPassword').value;
  const confirm  = document.getElementById('regConfirm').value;

  let valid = true;

  if (!name) {
    showFieldError('regName', 'regNameErr', 'Full name is required');
    valid = false;
  }
  if (!email) {
    showFieldError('regEmail', 'regEmailErr', 'Email is required');
    valid = false;
  } else if (!isValidEmail(email)) {
    showFieldError('regEmail', 'regEmailErr', 'Enter a valid email');
    valid = false;
  }
  if (!password) {
    showFieldError('regPassword', 'regPasswordErr', 'Password is required');
    valid = false;
  } else if (password.length < 8) {
    showFieldError('regPassword', 'regPasswordErr', 'Password must be at least 8 characters');
    valid = false;
  }
  if (!confirm) {
    showFieldError('regConfirm', 'regConfirmErr', 'Please confirm your password');
    valid = false;
  } else if (password !== confirm) {
    showFieldError('regConfirm', 'regConfirmErr', 'Passwords do not match');
    valid = false;
  }

  if (!valid) return;

  setLoading('registerBtnText', 'registerSpinner', true);

  try {
    const response = await fetch(`${API_URL}/api/auth/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name, email, password })
    });

    const data = await response.json();

    if (!response.ok || !data.token) {
      showBanner('registerBanner', data.message || 'Registration failed.');
    } else {
      // Auto login after register
      localStorage.setItem('token',     data.token);
      localStorage.setItem('userName',  data.name);
      localStorage.setItem('userEmail', data.email);

      showBanner('registerSuccess', `Account created! Welcome, ${data.name}! Redirecting...`);
      setTimeout(() => {
        window.location.href = 'index.html';
      }, 1000);
    }

  } catch (err) {
    showBanner('registerBanner', '❌ Cannot connect to server. Make sure backend is running.');
  }

  setLoading('registerBtnText', 'registerSpinner', false);
}


// ==============================
// FORGOT PASSWORD
// ==============================

async function handleForgot() {
  clearAllErrors();

  const email = document.getElementById('forgotEmail').value.trim();

  if (!email) {
    showFieldError('forgotEmail', 'forgotEmailErr', 'Email is required');
    return;
  }
  if (!isValidEmail(email)) {
    showFieldError('forgotEmail', 'forgotEmailErr', 'Enter a valid email');
    return;
  }

  setLoading('forgotBtnText', 'forgotSpinner', true);
  await new Promise(r => setTimeout(r, 1000));

  // Forgot password needs email service — we'll add this later
  showBanner('forgotSuccess', '✅ If this email exists, a reset link will be sent!');
  setLoading('forgotBtnText', 'forgotSpinner', false);
}