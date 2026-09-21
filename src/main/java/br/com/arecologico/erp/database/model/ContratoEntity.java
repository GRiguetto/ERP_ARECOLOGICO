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
 * 1. @Entity & @Table:
 *    - Mapeia a tabela 'CONTRATO' do banco de dados relacional.
 * 
 * 2. @ManyToOne:
 *    - Relacionamento N:1 com TipoContratoEntity (chave estrangeira COD_TIPO_CONTRATO).
 *    - Relacionamento N:1 com PessoaEntity / PessoaClienteEntity (chave estrangeira COD_PESSOA).
 *    - fetch = FetchType.LAZY: Carrega sob demanda para alta performance.
 * 
 * 3. @JoinColumn:
 *    - Mapeia a coluna FK física no banco de dados.
 * 
 * 4. @ManyToMany & @JoinTable (Slide 17 do PDF):
 *    - Relacionamento N:N entre CONTRATO e EQUIPAMENTO mapeado através da tabela
 *      de junção 'CONTRATO_EQUIPAMENTO'.
 *    - Relacionamento N:N entre CONTRATO e ORDEM_SERVICO mapeado através da tabela
 *      de junção 'CONTRATO_OS'.
 *    - Parâmetros: joinColumns (chave desta entidade) e inverseJoinColumns (chave da entidade associada).
 * ============================================================================
 */
@Entity(name = "ContratoEntity")
@Table(name = "CONTRATO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContratoEntity {

    /**
     * Chave primária: COD_CONTRATO INT NOT NULL IDENTITY(1,1)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_CONTRATO")
    private Integer cod_Contrato;

    /**
     * Descrição dos termos e escopo do contrato de manutenção
     * dba.sql: DESCRICAO_CONTRATO VARCHAR(500) NULL
     */
    @Column(name = "DESCRICAO_CONTRATO", length = 500, nullable = true)
    private String descricao;

    /**
     * Valor total ou mensal do contrato (dba.sql: VALOR_CONTRATO DECIMAL(10,2) NOT NULL)
     */
    @Column(name = "VALOR_CONTRATO", precision = 10, scale = 2, nullable = false)
    private Float valor_Contrato;

    /**
     * Data de início da vigência do contrato (dba.sql: DATA_CONTRATO_INICIO DATE NOT NULL)
     */
    @Column(name = "DATA_CONTRATO_INICIO", nullable = false)
    private LocalDate data_Con_Inicio;

    /**
     * Data de término da vigência do contrato (dba.sql: DATA_CONTRATO_FIM DATE NOT NULL)
     */
    @Column(name = "DATA_CONTRATO_FIM", nullable = false)
    private LocalDate data_Con_Fim;

    /**
     * Tipo do Contrato (FK -> TIPO_CONTRATO.COD_TIPO_CONTRATO)
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COD_TIPO_CONTRATO", nullable = false)
    private TipoContratoEntity tipo_Contrato;

    /**
     * Cliente contratante (FK -> CLIENTE.COD_PESSOA / PESSOA.COD_PESSOA)
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COD_PESSOA", nullable = false)
    private PessoaEntity pessoa;

    /**
     * Relacionamento N:N com EQUIPAMENTO via tabela associativa 'CONTRATO_EQUIPAMENTO'
     * (Demonstração da anotação @JoinTable do Slide 17 do PDF)
     */
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "CONTRATO_EQUIPAMENTO",
        joinColumns = @JoinColumn(name = "COD_CONTRATO"),
        inverseJoinColumns = @JoinColumn(name = "COD_EQUIPAMENTO")
    )
    private List<EquipamentoEntity> equipamentos = new ArrayList<>();

    /**
     * Relacionamento N:N com ORDEM_SERVICO via tabela associativa 'CONTRATO_OS'
     */
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "CONTRATO_OS",
        joinColumns = @JoinColumn(name = "COD_CONTRATO"),
        inverseJoinColumns = @JoinColumn(name = "COD_OS")
    )
    private List<OSEntity> ordensServico = new ArrayList<>();

    /**
     * Construtor de conveniência para inicialização sem listas de relacionamentos N:N
     */
    public ContratoEntity(Integer cod_Contrato, String descricao, Float valor_Contrato,
                          LocalDate data_Con_Inicio, LocalDate data_Con_Fim,
                          TipoContratoEntity tipo_Contrato, PessoaEntity pessoa) {
        this.cod_Contrato = cod_Contrato;
        this.descricao = descricao;
        this.valor_Contrato = valor_Contrato;
        this.data_Con_Inicio = data_Con_Inicio;
        this.data_Con_Fim = data_Con_Fim;
        this.tipo_Contrato = tipo_Contrato;
        this.pessoa = pessoa;
        this.equipamentos = new ArrayList<>();
        this.ordensServico = new ArrayList<>();
    }
}
