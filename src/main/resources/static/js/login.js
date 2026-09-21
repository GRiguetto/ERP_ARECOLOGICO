/**
 * ============================================================
 *  AR ECOLÓGICO — LOGIN & CADASTRO
 *  Arquivo: js/login.js
 *  Modo: Frontend only (sem backend)
 * ============================================================
 */

// ── Utilitários de UI ─────────────────────────────────────────
function setLoading(btn, loading, label = 'Aguarde...') {
    btn.disabled = loading;
    btn.textContent = loading ? label : btn.dataset.originalText;
}

function showError(formId, msg) {
    const form = document.getElementById(formId);
    let el = form.querySelector('.api-error');
    if (!el) {
        el = document.createElement('p');
        el.className = 'api-error';
        el.style.cssText = 'color:#c0392b;background:#fdecea;padding:10px 14px;border-radius:6px;margin-bottom:12px;font-size:.9rem;';
        form.prepend(el);
    }
    el.textContent = msg;
    el.style.display = 'block';
}

function hideError(formId) {
    const el = document.getElementById(formId)?.querySelector('.api-error');
    if (el) el.style.display = 'none';
}

// ── Validações ────────────────────────────────────────────────
function isEmailValido(email) {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
}

function isTelefoneValido(tel) {
    // Aceita (17) 99999-9999 ou (17) 9999-9999
    return /^\(\d{2}\)\s\d{4,5}-\d{4}$/.test(tel);
}

// ── Máscara de telefone: (17) 99999-9999 ─────────────────────
function aplicarMascaraTelefone(input) {
    input.addEventListener('input', () => {
        let v = input.value.replace(/\D/g, '').slice(0, 11);
        if (v.length > 10) {
            v = v.replace(/^(\d{2})(\d{5})(\d{4})$/, '($1) $2-$3');
        } else if (v.length > 6) {
            v = v.replace(/^(\d{2})(\d{4})(\d*)$/, '($1) $2-$3');
        } else if (v.length > 2) {
            v = v.replace(/^(\d{2})(\d*)$/, '($1) $2');
        } else if (v.length > 0) {
            v = v.replace(/^(\d*)$/, '($1');
        }
        input.value = v;
    });
}

// ── Feedback visual por campo ─────────────────────────────────
function setFieldError(input, msg) {
    input.style.borderColor = '#c0392b';
    let hint = input.parentElement.querySelector('.field-hint');
    if (!hint) {
        hint = document.createElement('span');
        hint.className = 'field-hint';
        hint.style.cssText = 'color:#c0392b;font-size:.8rem;margin-top:4px;display:block;';
        input.parentElement.appendChild(hint);
    }
    hint.textContent = msg;
}

function clearFieldError(input) {
    input.style.borderColor = '';
    const hint = input.parentElement.querySelector('.field-hint');
    if (hint) hint.textContent = '';
}

// ── Lógica principal ──────────────────────────────────────────
document.addEventListener('DOMContentLoaded', () => {

    document.querySelectorAll('button[type="submit"]').forEach(btn => {
        btn.dataset.originalText = btn.textContent;
    });

    // ── Aplicar máscaras ──────────────────────────────────────
    aplicarMascaraTelefone(document.getElementById('reg_telefone'));

    // ── Validação em tempo real — e-mail login ────────────────
    const loginUserInput = document.getElementById('login_user');
    loginUserInput.addEventListener('blur', () => {
        const v = loginUserInput.value.trim();
        if (v && !isEmailValido(v)) {
            setFieldError(loginUserInput, 'E-mail inválido.');
        } else {
            clearFieldError(loginUserInput);
        }
    });
    loginUserInput.addEventListener('input', () => clearFieldError(loginUserInput));

    // ── Validação em tempo real — e-mail cadastro ─────────────
    const regEmailInput = document.getElementById('reg_email');
    regEmailInput.addEventListener('blur', () => {
        const v = regEmailInput.value.trim();
        if (v && !isEmailValido(v)) {
            setFieldError(regEmailInput, 'E-mail inválido.');
        } else {
            clearFieldError(regEmailInput);
        }
    });
    regEmailInput.addEventListener('input', () => clearFieldError(regEmailInput));

    // ── Validação em tempo real — telefone ────────────────────
    const telInput = document.getElementById('reg_telefone');
    telInput.addEventListener('blur', () => {
        const v = telInput.value.trim();
        if (v && !isTelefoneValido(v)) {
            setFieldError(telInput, 'Telefone incompleto.');
        } else {
            clearFieldError(telInput);
        }
    });
    telInput.addEventListener('input', () => clearFieldError(telInput));

    // ── Validação em tempo real — confirmação de senha ────────
    const regPassConfirm = document.getElementById('reg_pass_confirm');
    regPassConfirm.addEventListener('blur', () => {
        const senha    = document.getElementById('reg_pass').value;
        const confirma = regPassConfirm.value;
        if (confirma && senha !== confirma) {
            setFieldError(regPassConfirm, 'As senhas não coincidem.');
        } else {
            clearFieldError(regPassConfirm);
        }
    });
    regPassConfirm.addEventListener('input', () => clearFieldError(regPassConfirm));

    // ── Alternar Login / Cadastro ──────────────────────────────
    document.getElementById('showRegister').addEventListener('click', e => {
        e.preventDefault();
        document.getElementById('loginBox').style.display = 'none';
        document.getElementById('registerBox').style.display = 'block';
    });

    document.getElementById('showLogin').addEventListener('click', e => {
        e.preventDefault();
        document.getElementById('registerBox').style.display = 'none';
        document.getElementById('loginBox').style.display = 'block';
    });

    // ── LOGIN ──────────────────────────────────────────────────
    document.getElementById('loginForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        hideError('loginForm');

        const email = document.getElementById('login_user').value.trim();
        const senha = document.getElementById('login_pass').value;

        if (!email) { showError('loginForm', 'Informe seu e-mail.'); return; }
        if (!isEmailValido(email)) { showError('loginForm', 'Informe um e-mail válido.'); return; }
        if (!senha)  { showError('loginForm', 'Informe sua senha.'); return; }

        const btn = e.target.querySelector('button[type="submit"]');
        setLoading(btn, true, 'Entrando...');

        await new Promise(r => setTimeout(r, 800));

        sessionStorage.setItem('ae_usuario', JSON.stringify({ nome: email, papel: 'admin' }));
        window.location.href = 'index.html';
    });

    // ── CADASTRO ───────────────────────────────────────────────
    document.getElementById('registerForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        hideError('registerForm');

        const nome     = document.getElementById('reg_nome').value.trim();
        const email    = document.getElementById('reg_email').value.trim();
        const telefone = document.getElementById('reg_telefone').value.trim();
        const senha    = document.getElementById('reg_pass').value;
        const confirma = document.getElementById('reg_pass_confirm').value;

        if (!nome)                  { showError('registerForm', 'Informe seu nome completo.'); return; }
        if (!email)                 { showError('registerForm', 'Informe seu e-mail.'); return; }
        if (!isEmailValido(email))  { showError('registerForm', 'Informe um e-mail válido.'); return; }
        if (!telefone)              { showError('registerForm', 'Informe seu telefone.'); return; }
        if (!isTelefoneValido(telefone)) { showError('registerForm', 'Telefone incompleto. Use (17) 99999-9999.'); return; }
        if (senha.length < 6)       { showError('registerForm', 'A senha deve ter no mínimo 6 caracteres.'); return; }
        if (senha !== confirma)     { showError('registerForm', 'As senhas não coincidem.'); return; }

        const btn = e.target.querySelector('button[type="submit"]');
        setLoading(btn, true, 'Cadastrando...');

        await new Promise(r => setTimeout(r, 800));

        setLoading(btn, false);
        alert('Cadastro realizado com sucesso! Faça seu login.');
        document.getElementById('showLogin').click();
    });
});