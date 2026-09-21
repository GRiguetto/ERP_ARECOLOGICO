package br.com.arecologico.erp.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * CONCEITOS SPRING DATA JPA / HIBERNATE (PDF - Slides 3, 4, 5, 6, 7, 19, 20, 21, 24, 28, 30):
 * ============================================================================
 * 1. @Entity: Declara a classe como entidade de persistência.
 * 
 * 2. @Table(name = "PESSOA"): Mapeia para a tabela base 'PESSOA' no banco de dados.
 * 
 * 3. @Inheritance(strategy = InheritanceType.JOINED):
 *    - Estratégia de HERANÇA JPA com tabelas separadas para cada classe da hierarquia.
 *    - Cria a tabela PESSOA com os dados comuns (nome, documento, contato, endereço)
 *      e tabelas filhas (CLIENTE e PRESTADOR_SERVICO) que compartilham a chave primária
 *      através de @PrimaryKeyJoinColumn.
 *    - Vantagem: Normalização perfeita dos dados, integridade referencial com chaves estrangeiras.
 * 
 * 4. @Embedded:
 *    - Incorpora os atributos da classe @Embeddable 'EnderecoEmbeddable' dentro da tabela PESSOA.
 * 
 * 5. @Transient:
 *    - Indica que o campo NÃO deve ser persistido no banco de dados (ignorado pelo JPA).
 *    - Útil para flags em memória, cálculos temporários ou formatações dinâmicas.
 * 
 * 6. @Version:
 *    - Usado para gerenciar CONCORRÊNCIA OTIMISTA (Optimistic Locking).
 *    - Evita que duas transações simultâneas sobrescrevam dados conflitantes sem detecção prévia.
 * ============================================================================
 */
@Entity(name = "PessoaEntity")
@Table(name = "PESSOA")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
public class PessoaEntity {

    /**
     * Chave primária da tabela PESSOA (COD_PESSOA INT NOT NULL IDENTITY(1,1))
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_PESSOA")
    private Integer cod_Pessoa;

    /**
     * Nome completo da pessoa física ou razão social da pessoa jurídica
     * dba.sql: NOME VARCHAR(150) NOT NULL
     */
    @Column(name = "NOME", length = 150, nullable = false)
    private String nome;

    /**
     * Cadastro de Pessoa Física (CPF - 11 dígitos)
     * dba.sql: CPF CHAR(11) NULL
     */
    @Column(name = "CPF", length = 11, nullable = true)
    private String CPF;

    /**
     * Registro Geral (RG)
     * dba.sql: RG VARCHAR(20) NULL
     */
    @Column(name = "RG", length = 20, nullable = true)
    private String RG;

    /**
     * Telefone de contato com DDD
     * dba.sql: TELEFONE VARCHAR(20) NULL
     */
    @Column(name = "TELEFONE", length = 20, nullable = true)
    private String telefone;

    /**
     * Objeto de Valor Embutido (@Embedded / @Embeddable - Slides 19-20 do PDF)
     * Agrupa: Logradouro, Número, CEP, Cidade e UF na mesma tabela PESSOA.
     */
    @Embedded
    private EnderecoEmbeddable dadosEndereco = new EnderecoEmbeddable();

    /**
     * Campo Transiente (@Transient - Slide 21 do PDF):
     * Não gera coluna no banco de dados. Usado exclusivamente para regras em memória.
     */
    @Transient
    private String informacaoTemporaria;

    /**
     * Controle de versão para concorrência otimista (@Version - Slide 21 do PDF):
     * O JPA incrementa automaticamente a cada UPDATE para prevenir conflitos de escrita simultânea.
     */
    @Version
    @Column(name = "VERSAO")
    private Long versao;

    /**
     * Construtor completo com parâmetros diretos para manter retrocompatibilidade
     */
    public PessoaEntity(Integer cod_Pessoa, String nome, String CPF, String RG, String telefone,
                        String CEP, String estado, String cidade, String endereco, String numero_Casa) {
        this.cod_Pessoa = cod_Pessoa;
        this.nome = nome;
        this.CPF = CPF;
        this.RG = RG;
        this.telefone = telefone;
        this.dadosEndereco = new EnderecoEmbeddable(endereco, numero_Casa, CEP, cidade, estado);
    }

    // Métodos de conveniência (Getters / Setters delegados para o EnderecoEmbeddable)
    public String getEndereco() {
        return dadosEndereco != null ? dadosEndereco.getEndereco() : null;
    }

    public void setEndereco(String endereco) {
        if (this.dadosEndereco == null) this.dadosEndereco = new EnderecoEmbeddable();
        this.dadosEndereco.setEndereco(endereco);
    }

    public String getNumero_Casa() {
        return dadosEndereco != null ? dadosEndereco.getNumeroCasa() : null;
    }

    public void setNumero_Casa(String numero_Casa) {
        if (this.dadosEndereco == null) this.dadosEndereco = new EnderecoEmbeddable();
        this.dadosEndereco.setNumeroCasa(numero_Casa);
    }

    public String getCEP() {
        return dadosEndereco != null ? dadosEndereco.getCep() : null;
    }

    public void setCEP(String CEP) {
        if (this.dadosEndereco == null) this.dadosEndereco = new EnderecoEmbeddable();
        this.dadosEndereco.setCep(CEP);
    }

    public String getCidade() {
        return dadosEndereco != null ? dadosEndereco.getCidade() : null;
    }

    public void setCidade(String cidade) {
        if (this.dadosEndereco == null) this.dadosEndereco = new EnderecoEmbeddable();
        this.dadosEndereco.setCidade(cidade);
    }

    public String getEstado() {
        return dadosEndereco != null ? dadosEndereco.getUf() : null;
    }

    public void setEstado(String estado) {
        if (this.dadosEndereco == null) this.dadosEndereco = new EnderecoEmbeddable();
        this.dadosEndereco.setUf(estado);
    }
}
