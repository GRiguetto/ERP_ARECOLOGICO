package br.com.arecologico.erp.database.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * CONCEITOS SPRING DATA JPA (PDF - Slides 3, 4, 5, 6, 7, 11, 17 e 18):
 * ============================================================================
 * 1. @Entity & @Table(name = "ORDEM_SERVICO"):
 *    - Mapeia a tabela central de Ordens de Serviço do ERP Arecológico.
 * 
 * 2. @ManyToOne com @JoinColumn:
 *    - Relacionamentos N:1 mapeando as chaves estrangeiras:
 *      * COD_PESSOA_CLIENTE -> PessoaClienteEntity
 *      * COD_PESSOA_PRESTADOR -> PrestadorServicoEntity
 *      * COD_TIPO_OS -> TipoOsEntity
 *      * COD_TIPO_PAGAMENTO -> TipoPagamentoEntity
 *    - Uso de 'fetch = FetchType.LAZY' para otimização de leitura.
 * 
 * 3. @ManyToMany com @JoinTable (Slide 17 do PDF):
 *    - Mapeia a tabela associativa 'ORDEM_SERVICO_SERVICO' relacionando
 *      a Ordem de Serviço aos Serviços realizados (N:N).
 * ============================================================================
 */
@Entity(name = "OSEntity")
@Table(name = "ORDEM_SERVICO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OSEntity {

    /**
     * Chave primária: COD_OS INT NOT NULL IDENTITY(1,1)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_OS")
    private Integer cod_Os;

    /**
     * Observações gerais sobre a OS (dba.sql: OBS VARCHAR(500) NULL)
     */
    @Column(name = "OBS", length = 500, nullable = true)
    private String obs;

    /**
     * Descrição do serviço executado pelo técnico (dba.sql: DESCRICAO_EXECUTADO VARCHAR(500) NULL)
     */
    @Column(name = "DESCRICAO_EXECUTADO", length = 500, nullable = true)
    private String descricao_Execucao;

    /**
     * Descrição do problema relatado pelo cliente (dba.sql: DESCRICAO_PROBLEMA VARCHAR(500) NULL)
     */
    @Column(name = "DESCRICAO_PROBLEMA", length = 500, nullable = true)
    private String descricao_Problema;

    /**
     * Data em que o serviço foi efetivamente executado
     */
    @Column(name = "DATA_EXECUCAO", nullable = true)
    private LocalDate data_Execucao;

    /**
     * Valor total apurado da Ordem de Serviço (dba.sql: VALOR_TOTAL DECIMAL(10,2) NULL)
     */
    @Column(name = "VALOR_TOTAL", precision = 10, scale = 2, nullable = true)
    private Float valor_Total;

    /**
     * Data de abertura/solicitação da OS (dba.sql: DATA_ABERTURA DATE NOT NULL)
     */
    @Column(name = "DATA_ABERTURA", nullable = false)
    private LocalDate data_Abertura;

    /**
     * Situação atual da OS ('Aberta', 'Em andamento', 'Concluída', 'Cancelada')
     * dba.sql: STATUS VARCHAR(50) NOT NULL
     */
    @Column(name = "STATUS", length = 50, nullable = false)
    private String status;

    /**
     * Cliente solicitante (FK -> CLIENTE.COD_PESSOA)
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COD_PESSOA_CLIENTE", nullable = false)
    private PessoaClienteEntity pessoa_Cliente;

    /**
     * Prestador de serviço / técnico responsável (FK -> PRESTADOR_SERVICO.COD_PESSOA)
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COD_PESSOA_PRESTADOR", nullable = false)
    private PrestadorServicoEntity prestador_Servico;

    /**
     * Tipo da Ordem de Serviço (FK -> TIPO_OS.COD_TIPO_OS)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COD_TIPO_OS", nullable = true)
    private TipoOsEntity tipo_Os;

    /**
     * Forma de Pagamento acordada (FK -> TIPO_PAGAMENTO.COD_TIPO_PAGAMENTO)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COD_TIPO_PAGAMENTO", nullable = true)
    private TipoPagamentoEntity tipo_Pagamento;

    /**
     * Lista de Serviços realizados nesta OS (N:N via ORDEM_SERVICO_SERVICO - Slide 17 do PDF)
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "ORDEM_SERVICO_SERVICO",
        joinColumns = @JoinColumn(name = "COD_OS"),
        inverseJoinColumns = @JoinColumn(name = "COD_SERVICO")
    )
    private List<ServicoEntity> servicos = new ArrayList<>();

    /**
     * Contratos associados a esta OS (N:N mapeado em CONTRATO_OS)
     */
    @JsonIgnore
    @ManyToMany(mappedBy = "ordensServico", fetch = FetchType.LAZY)
    private List<ContratoEntity> contratos = new ArrayList<>();

    /**
     * Construtor de conveniência para inicialização sem listas de coleções
     */
    public OSEntity(Integer cod_Os, String obs, String descricao_Execucao, String descricao_Problema,
                    LocalDate data_Execucao, Float valor_Total, LocalDate data_Abertura, String status,
                    PessoaClienteEntity pessoa_Cliente, PrestadorServicoEntity prestador_Servico,
                    TipoOsEntity tipo_Os, TipoPagamentoEntity tipo_Pagamento) {
        this.cod_Os = cod_Os;
        this.obs = obs;
        this.descricao_Execucao = descricao_Execucao;
        this.descricao_Problema = descricao_Problema;
        this.data_Execucao = data_Execucao;
        this.valor_Total = valor_Total;
        this.data_Abertura = data_Abertura;
        this.status = status;
        this.pessoa_Cliente = pessoa_Cliente;
        this.prestador_Servico = prestador_Servico;
        this.tipo_Os = tipo_Os;
        this.tipo_Pagamento = tipo_Pagamento;
        this.servicos = new ArrayList<>();
        this.contratos = new ArrayList<>();
    }
}
