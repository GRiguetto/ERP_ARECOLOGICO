/**
 * nav.js – AR Ecológico ERP
 *
 * Responsabilidade: injetar o menu lateral (sidebar) e a barra superior (topbar)
 * em todas as subpáginas do sistema, gerenciando os links de navegação de forma
 * centralizada. Assim, qualquer alteração no menu precisa ser feita em UM só lugar.
 *
 * Como usar numa página:
 *   1. Inclua este script no final do <body>
 *   2. Chame initLayout('id-da-secao', 'Título da Página', 'fa-icone')
 *
 * Exemplo (em os/nova.html):
 *   <script src="../../static/js/nav.js"></script>
 *   <script> initLayout('os', 'Nova Ordem de Serviço', 'fa-plus'); </script>
 */


// ============================================================
// BASE URL
// Caminho absoluto até a pasta templates, a partir da raiz
// do Live Server. Como o Live Server serve a partir da raiz
// do projeto, este caminho funciona independente de qual
// subpasta a página atual esteja (os/, clientes/, rh/, etc.)
// ============================================================
const base = '/src/main/resources/templates/';


// ============================================================
// DEFINIÇÃO DOS ITENS DE MENU
//
// Cada objeto representa um item do menu lateral.
// - id:      identificador único do item (usado para marcar o ativo)
// - icon:    classe do Font Awesome (ex: 'fa-house')
// - label:   texto exibido no menu
// - href:    caminho completo da página (usando a variável base)
// - submenu: array de subitens (quando o item tem filhos)
//
// IMPORTANTE: todos os hrefs usam a variável `base` para garantir
// que os links funcionem de qualquer subpasta do projeto.
// ============================================================
const menuItems = [

  // ── Dashboard (página inicial) ──
  {
    id: 'dashboard',
    icon: 'fa-house',
    label: 'Dashboard',
    href: base + 'index.html'
  },

  // ── Ordem de Serviço (com submenu) ──
  {
    id: 'os',
    icon: 'fa-clipboard-list',
    label: 'Ordem de Serviço',
    submenu: [
      { icon: 'fa-plus', label: 'Nova OS',   href: base + 'os/nova.html' },
      { icon: 'fa-list', label: 'Listar OS', href: base + 'os/lista.html' },
    ]
  },

  // ── Clientes (com submenu) ──
  {
    id: 'clientes',
    icon: 'fa-users',
    label: 'Clientes',
    submenu: [
      { icon: 'fa-user-plus', label: 'Cadastrar', href: base + 'clientes/cadastro.html' },
      { icon: 'fa-list',      label: 'Listar',    href: base + 'clientes/lista.html' },
    ]
  },

  // ── Serviços (página simples) ──
  {
    id: 'servicos',
    icon: 'fa-hammer',
    label: 'Serviços',
    href: base + 'servicos/lista.html'
  },

  // ── Estoque (página simples) ──
  {
    id: 'estoque',
    icon: 'fa-boxes-stacked',
    label: 'Estoque',
    href: base + 'estoque/index.html'
  },

  // ── Contratos (página simples) ──
  {
    id: 'contratos',
    icon: 'fa-file-signature',
    label: 'Contratos',
    href: base + 'contratos/lista.html'
  },

  // ── Financeiro (com submenu) ──
  {
    id: 'financeiro',
    icon: 'fa-money-bill-wave',
    label: 'Financeiro',
    submenu: [
      { icon: 'fa-arrow-down', label: 'Contas a Receber', href: base + 'financeiro/receber.html' },
      { icon: 'fa-arrow-up',   label: 'Contas a Pagar',   href: base + 'financeiro/pagar.html' },
      { icon: 'fa-chart-line', label: 'Fluxo de Caixa', href: base + 'relatorios/fluxo-caixa.html' },
      { icon: 'fa-receipt',    label: 'DRE',               href: base + 'relatorios/dre.html' },
    ]
  },

  // ── Relatórios (página simples) ──
  {
    id: 'relatorios',
    icon: 'fa-chart-column',
    label: 'Relatórios',
    href: base + 'relatorios/index.html'
  },

  // ── Funcionários / RH (com submenu) ──
  {
    id: 'rh',
    icon: 'fa-users-gear',
    label: 'Funcionários',
    submenu: [
      { icon: 'fa-user-tie', label: 'Cadastrar',          href: base + 'rh/cadastro.html' },
      { icon: 'fa-clock',    label: 'Controle de Ponto',  href: base + 'rh/ponto.html' },
      { icon: 'fa-star',     label: 'Desempenho',         href: base + 'rh/desempenho.html' },
    ]
  },

  // ── Fiscal / NFS-e (página simples) ──
  {
    id: 'fiscal',
    icon: 'fa-file-invoice',
    label: 'Fiscal / NFS-e',
    href: base + 'fiscal/index.html'
  },

  // ── Configurações (página simples) ──
  {
    id: 'config',
    icon: 'fa-gear',
    label: 'Configurações',
    href: base + 'configuracoes/index.html'
  },

  // ── Ajuda (com submenu) ──
  {
    id: 'ajuda',
    icon: 'fa-circle-question',
    label: 'Ajuda',
    submenu: [
      { icon: 'fa-book',    label: 'Documentação', href: '#' },
      { icon: 'fa-headset', label: 'Suporte',      href: '#' },
    ]
  },

];


// ============================================================
// FUNÇÃO: buildSidebar
//
// Constrói o HTML completo do menu lateral (sidebar).
//
// Parâmetro:
//   activePageId (string) – id do item de menu que deve
//   aparecer destacado como "ativo". Ex: 'os', 'clientes'.
//
// Retorna:
//   string com o HTML completo da <nav>.
// ============================================================
function buildSidebar(activePageId) {

  // Detecta qual subitem está ativo comparando a URL atual
  // com os hrefs dos submenus (usado para expandir o submenu certo)
  const currentPath = window.location.pathname;

  // Início do HTML do sidebar
  let html = `
    <nav class="nav-side-menu">

      <!-- Logotipo no topo do menu -->
      <div class="brand">
        <div class="brand-text">
          <i class="fa fa-leaf brand-icon"></i>
          <span>AR Ecológico</span>
        </div>
      </div>

      <!-- Botão hamburguer – visível só em mobile -->
      <i class="fa fa-bars fa-2x toggle-btn" data-toggle="collapse" data-target="#menu-content"></i>

      <!-- Lista de itens do menu -->
      <div class="menu-list">
        <ul id="menu-content" class="menu-content collapse out">
  `;

  // Percorre cada item definido em menuItems
  menuItems.forEach(item => {

    // Verifica se este item é o ativo (para adicionar classe CSS)
    const isActive = item.id === activePageId ? 'active' : '';

    if (item.submenu) {
      // ── Item COM submenu ──────────────────────────────────
      // Verifica se algum subitem corresponde à URL atual,
      // para expandir automaticamente o submenu correto
      const isOpen = item.submenu.some(sub =>
        sub.href !== '#' && currentPath.includes(sub.href.replace(base, ''))
      );

      // Gera o item pai (clicável para expandir/colapsar)
      html += `
        <li class="menu-item ${isOpen ? 'active' : ''}"
            data-toggle="collapse"
            data-target="#${item.id}">
          <a href="#">
            <i class="fa ${item.icon} sidebar-icon"></i>
            <span>${item.label}</span>
            <i class="fa fa-angle-down arrow-icon"></i>
          </a>
        </li>

        <!-- Submenu colapsável – começa aberto se isOpen = true -->
        <ul class="sub-menu collapse ${isOpen ? 'show' : ''}" id="${item.id}">
      `;

      // Gera cada subitem dentro do submenu
      item.submenu.forEach(sub => {
        html += `
          <li>
            <a href="${sub.href}">
              <i class="fa ${sub.icon} fa-fw"></i> ${sub.label}
            </a>
          </li>
        `;
      });

      html += `</ul>`; // fecha o <ul> do submenu

    } else {
      // ── Item SIMPLES (sem submenu) ────────────────────────
      html += `
        <li class="menu-item ${isActive}" data-id="${item.id}">
          <a href="${item.href}">
            <i class="fa ${item.icon} sidebar-icon"></i>
            <span>${item.label}</span>
          </a>
        </li>
      `;
    }
  });

  // Fecha as tags abertas do sidebar
  html += `
        </ul>
      </div>
    </nav>
  `;

  return html;
}


// ============================================================
// FUNÇÃO: buildTopbar
//
// Constrói o HTML da barra superior (header) da página,
// com o título da seção atual, seletor de competência e
// identificação do usuário logado.
//
// Parâmetros:
//   pageTitle (string) – Ex: 'Nova Ordem de Serviço'
//   pageIcon  (string) – Classe Font Awesome. Ex: 'fa-plus'
//
// Retorna:
//   string com o HTML do <header>.
// ============================================================
function buildTopbar(pageTitle, pageIcon) {
  return `
    <header class="topbar">

      <!-- Lado esquerdo: título da página com ícone -->
      <div class="topbar-left">
        <h1 class="page-title">
          <i class="fa ${pageIcon}"></i> ${pageTitle}
        </h1>
      </div>

      <!-- Lado direito: seletor de mês e usuário logado -->
      <div class="topbar-right">

        <!-- Seletor de competência (mês de referência) -->
        <div class="competencia-wrapper">
          <label for="competencia">
            <i class="fa fa-calendar-days"></i> Competência:
          </label>
          <select id="competencia" class="competencia-select">
            <option value="">— Selecione o mês —</option>
            <option value="2025-01">Janeiro / 2025</option>
            <option value="2025-02">Fevereiro / 2025</option>
            <option value="2025-03">Março / 2025</option>
            <option value="2025-04">Abril / 2025</option>
            <option value="2025-05">Maio / 2025</option>
            <option value="2025-06">Junho / 2025</option>
          </select>
        </div>

        <!-- Badge do usuário logado -->
        <div class="user-badge">
          <i class="fa fa-circle-user"></i>
          <span>Administrador</span>
        </div>

      </div>
    </header>
  `;
}


// ============================================================
// FUNÇÃO: initLayout
//
// Função principal — chamada em cada subpágina para montar
// o layout completo (sidebar + topbar).
//
// O que ela faz:
//   1. Gera o HTML do sidebar e injeta no início do <body>
//   2. Gera o HTML da topbar e injeta no início do <main id="pagina">
//   3. Configura o comportamento de clique do menu (marcar ativo)
//
// Parâmetros:
//   activePageId (string) – id do item de menu ativo. Ex: 'os'
//   pageTitle    (string) – título exibido na topbar. Ex: 'Nova OS'
//   pageIcon     (string) – ícone Font Awesome. Ex: 'fa-plus'
//
// Como usar (no final do HTML de cada subpágina):
//   initLayout('os', 'Nova Ordem de Serviço', 'fa-plus');
//   initLayout('clientes', 'Cadastrar Cliente', 'fa-user-plus');
//   initLayout('dashboard', 'Dashboard', 'fa-house');
// ============================================================
function initLayout(activePageId, pageTitle, pageIcon) {

  // 1. Gera e injeta o sidebar no início do <body>
  const sidebar = buildSidebar(activePageId);
  document.body.insertAdjacentHTML('afterbegin', sidebar);

  // 2. Gera e injeta a topbar no início do <main id="pagina">
  //    O <main> deve ter id="pagina" em todas as subpáginas
  const topbar = buildTopbar(pageTitle, pageIcon);
  const main = document.getElementById('pagina');
  if (main) {
    main.insertAdjacentHTML('afterbegin', topbar);
  }

  // 3. Ao clicar num item do menu, remove "active" de todos
  //    e adiciona no item clicado (feedback visual imediato)
  $(document).ready(function () {
    $('.menu-item > a').on('click', function () {
      $('.menu-item').removeClass('active');
      $(this).closest('.menu-item').addClass('active');
    });
  });

}