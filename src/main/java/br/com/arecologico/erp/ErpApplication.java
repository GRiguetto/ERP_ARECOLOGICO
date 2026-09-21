package br.com.arecologico.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import br.com.arecologico.erp.database.model.*;
import java.time.LocalDate;
import java.util.*;

/**
 * ============================================================================
 * CONCEITOS SPRING BOOT: @SpringBootApplication e Inicialização
 * ============================================================================
 * 1. @SpringBootApplication:
 *    Anotação fundamental que combina três anotações essenciais:
 *    - @Configuration: Marca a classe como fonte de definições de beans do Spring.
 *    - @EnableAutoConfiguration: Habilita a configuração automática do Spring Boot
 *      (detecta drivers JDBC, Hibernate/JPA, Tomcat, MVC, etc.).
 *    - @ComponentScan: Escaneia automaticamente todos os pacotes a partir deste
 *      (br.com.arecologico.erp.*) para registrar @Entity, @Repository, @Service, @Controller.
 * 
 * 2. SpringApplication.run():
 *    Inicializa o contexto da aplicação Spring (ApplicationContext), configura o
 *    servidor web embutido (se aplicável) e conecta o pool de conexões com o banco de dados.
 * ============================================================================
 */
@SpringBootApplication
public class ErpApplication {

	/**
	 * Ponto de entrada principal da aplicação Java
	 * @param args Argumentos de linha de comando
	 */
	public static void main(String[] args) {
		// Inicializa o contexto do Spring Boot
		SpringApplication.run(ErpApplication.class, args);

		// Inicializando dados mockados para demonstração e validação em memória
		ErpDataMock dataMock = new ErpDataMock();
		dataMock.inicializarDados();
	}

	/**
	 * Classe interna para gerenciar dados mockados com estruturas de dados em memória.
	 * Simula e valida as relações do modelo dba.sql e os conceitos de JPA.
	 */
	static class ErpDataMock {

		// Coleções em memória para armazenar as entidades
		private List<MarcaEntity> marcas;
		private List<EquipamentoEntity> equipamentos;
		private List<TipoContratoEntity> tiposContrato;
		private List<TipoPagamentoEntity> tiposPagamento;
		private List<TipoOsEntity> tiposOS;
		private List<ServicoEntity> servicos;
		private List<PessoaEntity> pessoas;
		private List<PessoaClienteEntity> clientes;
		private List<PrestadorServicoEntity> prestadores;
		private List<ContratoEntity> contratos;
		private List<OSEntity> ordensServico;

		// Mapas para simulação dos relacionamentos associativos N:N
		private Map<Integer, List<Integer>> contratoEquipamentos; // COD_CONTRATO -> lista COD_EQUIPAMENTO
		private Map<Integer, List<Integer>> osServicos; // COD_OS -> lista COD_SERVICO
		private Map<Integer, List<Integer>> contratoOS; // COD_CONTRATO -> lista COD_OS

		public ErpDataMock() {
			this.marcas = new ArrayList<>();
			this.equipamentos = new ArrayList<>();
			this.tiposContrato = new ArrayList<>();
			this.tiposPagamento = new ArrayList<>();
			this.tiposOS = new ArrayList<>();
			this.servicos = new ArrayList<>();
			this.pessoas = new ArrayList<>();
			this.clientes = new ArrayList<>();
			this.prestadores = new ArrayList<>();
			this.contratos = new ArrayList<>();
			this.ordensServico = new ArrayList<>();
			this.contratoEquipamentos = new HashMap<>();
			this.osServicos = new HashMap<>();
			this.contratoOS = new HashMap<>();
		}

		/**
		 * Executa a carga de todas as entidades e seus vínculos
		 */
		public void inicializarDados() {
			System.out.println("\n============================================");
			System.out.println("   INICIANDO CARREGAMENTO DE DADOS MOCKADOS   ");
			System.out.println("============================================\n");

			carregarMarcas();
			carregarEquipamentos();
			carregarTiposContrato();
			carregarTiposPagamento();
			carregarTiposOS();
			carregarServicos();
			carregarPessoas();
			carregarClientes();
			carregarPrestadores();
			carregarContratos();
			carregarRelacionamentosContratoEquipamento();
			carregarOrdensServico();
			carregarRelacionamentosOSServico();
			carregarRelacionamentosContratoOS();

			exibirResumo();
		}

		// =============== CARREGAMENTO DE MARCAS ===============
		private void carregarMarcas() {
			String[] nomeMarcas = { "LG", "Elgin", "Samsung", "Midea", "Daikin",
					"Springer", "Trane", "Carrier", "Hitachi", "Gree" };

			for (int i = 0; i < nomeMarcas.length; i++) {
				MarcaEntity marca = new MarcaEntity(i + 1, nomeMarcas[i]);
				marcas.add(marca);
			}
			System.out.println("[✓] " + marcas.size() + " Marcas carregadas");
		}

		// =============== CARREGAMENTO DE EQUIPAMENTOS ===============
		private void carregarEquipamentos() {
			String[] modelos = { "Split Hi-Wall S4NQ09WA3", "Split Hi-Wall S4NQ12WA3",
					"Split Eco HWFI09B2IA", "Split Eco HWFI12B2IA", "WindFree AR09BSHCBWKNAZ",
					"WindFree AR12BSHCBWKNAZ", "Olímpico Inverter 18000", "FTK09TVMW",
					"Springer Midea 24000", "Carrier X-Power 18000" };
			int[] btus = { 9000, 12000, 9000, 12000, 9000, 12000, 18000, 9000, 24000, 18000 };
			int[] codMarca = { 1, 1, 2, 2, 3, 3, 4, 5, 6, 8 };

			for (int i = 0; i < modelos.length; i++) {
				MarcaEntity marca = marcas.get(codMarca[i] - 1);
				EquipamentoEntity equip = new EquipamentoEntity(i + 1, modelos[i], btus[i], marca);
				equipamentos.add(equip);
			}
			System.out.println("[✓] " + equipamentos.size() + " Equipamentos carregados");
		}

		// =============== CARREGAMENTO DE TIPOS DE CONTRATO ===============
		private void carregarTiposContrato() {
			String[] tipos = { "Mensal", "Trimestral", "Semestral", "Anual", "Bianual",
					"Avulso", "Corporativo Mensal", "Corporativo Anual", "Emergencial",
					"Preventivo Plus" };

			for (int i = 0; i < tipos.length; i++) {
				TipoContratoEntity tipo = new TipoContratoEntity(i + 1, tipos[i]);
				tiposContrato.add(tipo);
			}
			System.out.println("[✓] " + tiposContrato.size() + " Tipos de Contrato carregados");
		}

		// =============== CARREGAMENTO DE TIPOS DE PAGAMENTO ===============
		private void carregarTiposPagamento() {
			String[] tipos = { "Pix", "Cartão de Crédito", "Cartão de Débito",
					"Boleto Bancário", "Transferência Bancária", "Dinheiro", "Cheque",
					"Crédito em Conta", "Parcelado no Cartão", "Convênio Empresarial" };

			for (int i = 0; i < tipos.length; i++) {
				TipoPagamentoEntity tipo = new TipoPagamentoEntity(i + 1, tipos[i]);
				tiposPagamento.add(tipo);
			}
			System.out.println("[✓] " + tiposPagamento.size() + " Tipos de Pagamento carregados");
		}

		// =============== CARREGAMENTO DE TIPOS DE OS ===============
		private void carregarTiposOS() {
			String[] tipos = { "Preventiva", "Corretiva", "Instalação", "Desinstalação",
					"Higienização", "Recarga de Gás", "Revisão Geral", "Garantia", "Orçamento",
					"Emergencial" };

			for (int i = 0; i < tipos.length; i++) {
				TipoOsEntity tipo = new TipoOsEntity(i + 1, tipos[i]);
				tiposOS.add(tipo);
			}
			System.out.println("[✓] " + tiposOS.size() + " Tipos de OS carregados");
		}

		// =============== CARREGAMENTO DE SERVIÇOS ===============
		private void carregarServicos() {
			Object[][] servicosData = {
					{ "Limpeza de filtro", 80.00f, "2025-01-01", null },
					{ "Higienização completa", 250.00f, "2025-01-01", null },
					{ "Recarga de gás R410A", 350.00f, "2025-01-01", null },
					{ "Instalação de equipamento", 400.00f, "2025-01-01", null },
					{ "Revisão elétrica", 180.00f, "2025-01-01", null },
					{ "Substituição de placa eletrônica", 600.00f, "2025-01-01", null },
					{ "Limpeza da serpentina", 200.00f, "2025-01-01", null },
					{ "Desinstalação de equipamento", 250.00f, "2025-01-01", null },
					{ "Manutenção preventiva completa", 320.00f, "2025-01-01", null },
					{ "Troca de capacitor", 150.00f, "2025-01-01", null } };

			for (int i = 0; i < servicosData.length; i++) {
				String descricao = (String) servicosData[i][0];
				Float valor = ((Number) servicosData[i][1]).floatValue();
				LocalDate dataInicio = LocalDate.parse((String) servicosData[i][2]);
				LocalDate dataFim = (LocalDate) servicosData[i][3];

				ServicoEntity servico = new ServicoEntity(i + 1, descricao, valor, dataInicio, dataFim);
				servicos.add(servico);
			}
			System.out.println("[✓] " + servicos.size() + " Serviços carregados");
		}

		// =============== CARREGAMENTO DE PESSOAS ===============
		private void carregarPessoas() {
			Object[][] pessoasData = {
					{ "Carlos Eduardo Souza", "12345678901", "1234567", "Rua das Flores", "100",
							"15025010", "São José do Rio Preto", "SP", "17991110001" },
					{ "Ana Paula Lima", "23456789012", "2345678", "Av. Bady Bassitt", "250",
							"15025020", "São José do Rio Preto", "SP", "17992220002" },
					{ "TechFrio Ltda", "34567890113", "3456789", "Rua do Comércio", "10",
							"15025030", "São José do Rio Preto", "SP", "17993330003" },
					{ "Marcos Antônio Reis", "45678901214", "4567890", "Rua孔XV de Novembro",
							"300", "15025040", "Mirassol", "SP", "17994440004" },
					{ "Fernanda Costa", "56789012315", "5678901", "Av. Alberto Andaló", "400",
							"15025050", "São José do Rio Preto", "SP", "17995550005" },
					{ "Climatize Soluções", "67890123416", "6789012", "Rua Pio XII", "55",
							"15025060", "Catanduva", "SP", "17996660006" },
					{ "João Pedro Alves", "78901234517", "7890123", "Rua Sete de Setembro",
							"120", "15025070", "São José do Rio Preto", "SP", "17997770007" },
					{ "Refrigeração Norte ME", "89012345618", "8901234", "Av. Philadélfia", "800",
							"15025080", "São José do Rio Preto", "SP", "17998880008" },
					{ "Beatriz Mendonça", "90123456719", "9012345", "Rua Floriano Peixoto", "90",
							"15025090", "Votuporanga", "SP", "17999990009" },
					{ "Roberto Faria", "01234567820", "0123456", "Rua Marechal Rondon", "45",
							"15025100", "São José do Rio Preto", "SP", "17990000010" } };

			for (int i = 0; i < pessoasData.length; i++) {
				PessoaEntity pessoa = new PessoaEntity(i + 1, (String) pessoasData[i][0],
						(String) pessoasData[i][1], (String) pessoasData[i][2],
						(String) pessoasData[i][8], (String) pessoasData[i][5],
						(String) pessoasData[i][7], (String) pessoasData[i][6],
						(String) pessoasData[i][3], (String) pessoasData[i][4]);
				pessoas.add(pessoa);
			}

			// Adiciona técnicos (prestadores)
			String[] nomeTecnicos = { "Lucas Técnico Silva", "Pedro Técnico Rocha", "Rafael Técnico Melo",
					"Diego Técnico Nunes", "Thiago Técnico Pires", "Bruno Técnico Castro",
					"André Técnico Ramos", "Felipe Técnico Sousa", "Vitor Técnico Lima",
					"Guilherme Técnico Dias" };

			for (int i = 0; i < nomeTecnicos.length; i++) {
				PessoaEntity pessoa = new PessoaEntity(i + 11, nomeTecnicos[i],
						String.format("%011d", 11000000000L + i), String.format("%07d", 1110000 + i),
						String.format("1799%07d", i + 1111111), "1500000" + i,
						"SP", "São José do Rio Preto", "Rua " + (char) ('A' + i), String.valueOf(i + 1));
				pessoas.add(pessoa);
			}

			System.out.println("[✓] " + pessoas.size() + " Pessoas carregadas");
		}

		// =============== CARREGAMENTO DE CLIENTES ===============
		private void carregarClientes() {
			Object[][] clientesData = {
					{ 1, "carlos.souza@email.com", "F", "1985-03-15" },
					{ 2, "ana.lima@email.com", "F", "1990-07-22" },
					{ 3, "contato@techfrio.com.br", "J", null },
					{ 4, "marcos.reis@email.com", "F", "1978-11-05" },
					{ 5, "fernanda.costa@email.com", "F", "1995-02-28" },
					{ 6, "contato@climatize.com.br", "J", null },
					{ 7, "joao.alves@email.com", "F", "1988-09-10" },
					{ 8, "contato@refrinorte.com.br", "J", null },
					{ 9, "beatriz.mendonca@email.com", "F", "2000-06-18" },
					{ 10, "roberto.faria@email.com", "F", "1972-12-30" } };

			for (Object[] data : clientesData) {
				Integer codPessoa = (Integer) data[0];
				String email = (String) data[1];
				String tipo = (String) data[2];
				String dataNasc = (String) data[3];

				PessoaEntity pessoaBase = pessoas.get(codPessoa - 1);
				PessoaClienteEntity cliente = new PessoaClienteEntity(pessoaBase.getCod_Pessoa(),
						pessoaBase.getNome(), pessoaBase.getCPF(), pessoaBase.getRG(),
						pessoaBase.getTelefone(), pessoaBase.getCEP(), pessoaBase.getEstado(),
						pessoaBase.getCidade(), pessoaBase.getEndereco(), pessoaBase.getNumero_Casa(),
						email, tipo, dataNasc != null ? LocalDate.parse(dataNasc) : null);
				clientes.add(cliente);
			}

			System.out.println("[✓] " + clientes.size() + " Clientes carregados");
		}

		// =============== CARREGAMENTO DE PRESTADORES ===============
		private void carregarPrestadores() {
			Float[] pagamentos = { 1800.00f, 2000.00f, 1600.00f, 2200.00f, 1900.00f,
					2100.00f, 1750.00f, 2300.00f, 1850.00f, 2050.00f };

			for (int i = 0; i < 10; i++) {
				PessoaEntity pessoaBase = pessoas.get(i + 10); // técnicos começam em índice 10
				PrestadorServicoEntity prestador = new PrestadorServicoEntity(
						pessoaBase.getCod_Pessoa(), pessoaBase.getNome(), pessoaBase.getCPF(),
						pessoaBase.getRG(), pessoaBase.getTelefone(), pessoaBase.getCEP(),
						pessoaBase.getEstado(), pessoaBase.getCidade(), pessoaBase.getEndereco(),
						pessoaBase.getNumero_Casa(), pagamentos[i]);
				prestadores.add(prestador);
			}

			System.out.println("[✓] " + prestadores.size() + " Prestadores de Serviço carregados");
		}

		// =============== CARREGAMENTO DE CONTRATOS ===============
		private void carregarContratos() {
			Object[][] contratosData = {
					{ "Manutenção preventiva mensal - TechFrio", 1500.00f, "2025-01-01", "2025-01-31", 1,
							3 },
					{ "Contrato trimestral residencial - Carlos", 900.00f, "2025-01-01", "2025-03-31", 2,
							1 },
					{ "Contrato semestral - Climatize", 3200.00f, "2025-01-01", "2025-06-30", 3, 6 },
					{ "Contrato anual corporativo - Refri Norte", 8000.00f, "2025-01-01", "2025-12-31",
							4, 8 },
					{ "Plano preventivo plus - Ana Paula", 600.00f, "2025-02-01", "2026-01-31", 10, 2 },
					{ "Contrato mensal - João Pedro", 450.00f, "2025-03-01", "2025-03-31", 1, 7 },
					{ "Renovação trimestral - TechFrio", 1500.00f, "2025-02-01", "2025-04-30", 2, 3 },
					{ "Contrato bianual - Marcos Reis", 5000.00f, "2025-01-01", "2026-12-31", 5, 4 },
					{ "Plano semestral - Fernanda Costa", 750.00f, "2025-04-01", "2025-09-30", 3, 5 },
					{ "Contrato anual - Beatriz Mendonça", 1200.00f, "2025-05-01", "2026-04-30", 4, 9 } };

			for (int i = 0; i < contratosData.length; i++) {
				String descricao = (String) contratosData[i][0];
				Float valor = (Float) contratosData[i][1];
				LocalDate dataInicio = LocalDate.parse((String) contratosData[i][2]);
				LocalDate dataFim = LocalDate.parse((String) contratosData[i][3]);
				Integer codTipo = (Integer) contratosData[i][4];
				Integer codCliente = (Integer) contratosData[i][5];

				PessoaEntity pessoa = pessoas.get(codCliente - 1);
				TipoContratoEntity tipo = tiposContrato.get(codTipo - 1);

				ContratoEntity contrato = new ContratoEntity(i + 1, descricao, valor, dataInicio,
						dataFim, tipo, pessoa);
				contratos.add(contrato);
			}

			System.out.println("[✓] " + contratos.size() + " Contratos carregados");
		}

		// =============== CARREGAMENTO DE RELACIONAMENTOS CONTRATO-EQUIPAMENTO ===============
		private void carregarRelacionamentosContratoEquipamento() {
			int[][] relacionamentos = { { 1, 1 }, { 1, 2 }, { 2, 3 }, { 3, 4 }, { 3, 5 },
					{ 4, 1 }, { 4, 6 }, { 5, 7 }, { 7, 1 }, { 7, 2 } };

			for (int[] rel : relacionamentos) {
				int codContrato = rel[0];
				int codEquipamento = rel[1];
				contratoEquipamentos.computeIfAbsent(codContrato, k -> new ArrayList<>())
						.add(codEquipamento);
			}

			System.out.println("[✓] " + relacionamentos.length + " Relacionamentos Contrato-Equipamento carregados");
		}

		// =============== CARREGAMENTO DE ORDENS DE SERVIÇO ===============
		private void carregarOrdensServico() {
			Object[][] osData = {
					{ "Limpeza mensal programada", 80.00f, "2025-01-10", "Concluída", "2025-01-10",
							"Filtro sujo", "Filtro limpo", 3, 11, 1, 1 },
					{ "Ar não gela", 350.00f, "2025-01-15", "Concluída", "2025-01-16",
							"Falta de gás R410A", "Recarga de gás realizada", 1, 12, 2, 4 },
					{ "Instalação novo equipamento", 400.00f, "2025-01-20", "Concluída", "2025-01-21",
							"Novo equipamento", "Equipamento instalado", 6, 13, 3, 1 },
					{ "Revisão preventiva trimestral", 320.00f, "2025-02-05", "Concluída", "2025-02-05",
							"Revisão de rotina", "Manutenção preventiva ok", 8, 14, 1, 2 },
					{ "Barulho estranho no equipamento", 150.00f, "2025-02-12", "Concluída", "2025-02-13",
							"Capacitor com defeito", "Capacitor substituído", 2, 15, 2, 1 },
					{ "Higienização completa solicitada", 250.00f, "2025-03-01", "Concluída", "2025-03-01",
							"Higienização programada", "Higienização concluída", 7, 16, 5, 4 },
					{ "Placa com defeito", 600.00f, "2025-03-10", "Em andamento", null,
							"Placa eletrônica queimada", null, 4, 17, 2, null },
					{ "OS sem contrato - cliente avulso", 80.00f, "2025-03-15", "Concluída", "2025-03-15",
							"Filtro entupido", "Limpeza realizada", 10, 18, 1, 1 },
					{ "Revisão semestral contrato Beatriz", 320.00f, "2025-05-10", "Aberta", null,
							"Revisão programada", null, 9, 19, 1, null },
					{ "Limpeza mensal renovação TechFrio", 80.00f, "2025-02-10", "Concluída", "2025-02-10",
							"Filtro sujo", "Filtro limpo", 3, 20, 1, 1 } };

			for (int i = 0; i < osData.length; i++) {
				String obs = (String) osData[i][0];
				Float valorTotal = (Float) osData[i][1];
				LocalDate dataAbertura = LocalDate.parse((String) osData[i][2]);
				String status = (String) osData[i][3];
				String dataExecStr = (String) osData[i][4];
				LocalDate dataExecucao = dataExecStr != null ? LocalDate.parse(dataExecStr) : null;
				String descProblema = (String) osData[i][5];
				String descExecutado = (String) osData[i][6];
				Integer codCliente = (Integer) osData[i][7];
				Integer codPrestador = (Integer) osData[i][8];
				Integer codTipoOS = (Integer) osData[i][9];
				Integer codTipoPagamento = (Integer) osData[i][10];

				PessoaClienteEntity cliente = clientes.get(codCliente - 1);
				PrestadorServicoEntity prestador = prestadores.get(codPrestador - 11);
				TipoOsEntity tipoOS = tiposOS.get(codTipoOS - 1);
				TipoPagamentoEntity tipoPagamento = codTipoPagamento != null
						? tiposPagamento.get(codTipoPagamento - 1)
						: null;

				OSEntity os = new OSEntity(i + 1, obs, descExecutado, descProblema, dataExecucao,
						valorTotal, dataAbertura, status, cliente, prestador, tipoOS, tipoPagamento);
				ordensServico.add(os);
			}

			System.out.println("[✓] " + ordensServico.size() + " Ordens de Serviço carregadas");
		}

		// =============== CARREGAMENTO DE RELACIONAMENTOS OS-SERVIÇO ===============
		private void carregarRelacionamentosOSServico() {
			int[][] relacionamentos = { { 1, 1 }, { 2, 3 }, { 3, 4 }, { 4, 9 }, { 4, 5 },
					{ 5, 10 }, { 6, 2 }, { 7, 6 }, { 8, 1 }, { 10, 1 } };

			for (int[] rel : relacionamentos) {
				int codOS = rel[0];
				int codServico = rel[1];
				osServicos.computeIfAbsent(codOS, k -> new ArrayList<>()).add(codServico);
			}

			System.out.println("[✓] " + relacionamentos.length + " Relacionamentos OS-Serviço carregados");
		}

		// =============== CARREGAMENTO DE RELACIONAMENTOS CONTRATO-OS ===============
		private void carregarRelacionamentosContratoOS() {
			int[][] relacionamentos = { { 1, 1 }, { 2, 2 }, { 3, 3 }, { 4, 4 }, { 5, 5 },
					{ 6, 6 }, { 8, 7 }, { 10, 9 }, { 7, 10 }, { 1, 10 } };

			for (int[] rel : relacionamentos) {
				int codContrato = rel[0];
				int codOS = rel[1];
				contratoOS.computeIfAbsent(codContrato, k -> new ArrayList<>()).add(codOS);
			}

			System.out.println("[✓] " + relacionamentos.length + " Relacionamentos Contrato-OS carregados");
		}

		// =============== EXIBIÇÃO DE RESUMO ===============
		private void exibirResumo() {
			System.out.println("\n============================================");
			System.out.println("          RESUMO DE DADOS CARREGADOS          ");
			System.out.println("============================================");
			System.out.println("Marcas:                    " + marcas.size());
			System.out.println("Equipamentos:              " + equipamentos.size());
			System.out.println("Tipos de Contrato:         " + tiposContrato.size());
			System.out.println("Tipos de Pagamento:        " + tiposPagamento.size());
			System.out.println("Tipos de OS:               " + tiposOS.size());
			System.out.println("Serviços:                  " + servicos.size());
			System.out.println("Pessoas:                   " + pessoas.size());
			System.out.println("Clientes:                  " + clientes.size());
			System.out.println("Prestadores:               " + prestadores.size());
			System.out.println("Contratos:                 " + contratos.size());
			System.out.println("Ordens de Serviço:         " + ordensServico.size());
			System.out.println("Relacionamentos (N:N):     "
					+ (contratoEquipamentos.size() + osServicos.size() + contratoOS.size()));
			System.out.println("============================================\n");

			exibirExemplosRelacionamentos();
		}

		// =============== EXIBIÇÃO DE EXEMPLOS DE RELACIONAMENTOS ===============
		private void exibirExemplosRelacionamentos() {
			System.out.println("=== EXEMPLOS DE RELACIONAMENTOS ===\n");

			// Exemplo 1: Contrato com Equipamentos
			System.out.println("📋 CONTRATO #1 - Equipamentos:");
			ContratoEntity contrato1 = contratos.get(0);
			System.out.println("   Descrição: " + contrato1.getDescricao());
			System.out.println("   Valor: R$ " + String.format("%.2f", contrato1.getValor_Contrato()));
			if (contratoEquipamentos.containsKey(1)) {
				System.out.println("   Equipamentos: " + contratoEquipamentos.get(1));
			}
			System.out.println();

			// Exemplo 2: OS com Serviços
			System.out.println("🔧 ORDEM DE SERVIÇO #1 - Serviços:");
			OSEntity os1 = ordensServico.get(0);
			System.out.println("   Observação: " + os1.getObs());
			System.out.println("   Status: " + os1.getStatus());
			System.out.println("   Valor: R$ " + String.format("%.2f", os1.getValor_Total()));
			if (osServicos.containsKey(1)) {
				System.out.println("   Serviços: " + osServicos.get(1));
			}
			System.out.println();

			// Exemplo 3: Contrato com OS
			System.out.println("📊 CONTRATO #1 - Ordens de Serviço:");
			System.out.println("   Contrato: " + contrato1.getDescricao());
			if (contratoOS.containsKey(1)) {
				System.out.println("   Ordens de Serviço: " + contratoOS.get(1));
			}
			System.out.println("\n");
		}
	}
}
