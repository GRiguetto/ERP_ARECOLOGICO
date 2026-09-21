package br.com.arecologico.erp.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * CONCEITO JPA DO PDF (Slide 19 e 20): @Embeddable
 * ============================================================================
 * A anotação @Embeddable indica que esta classe NÃO é uma entidade com tabela
 * própria, mas sim uma classe cujos atributos são EMBUTIDOS/INCORPORADOS na
 * tabela de outra entidade proprietária (como PessoaEntity).
 * 
 * Vantagens:
 * 1. Reutilização de código e organização orientada a objetos (DDD - Value Object).
 * 2. Facilita o agrupamento lógico de campos de endereço (Rua, Número, CEP, etc.)
 *    mantendo-os na mesma tabela 'PESSOA' do banco de dados relacional.
 * ============================================================================
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EnderecoEmbeddable {

    /**
     * Logradouro / Rua / Avenida
     * Mapeado para a coluna 'ENDERECO' na tabela PESSOA (dba.sql: VARCHAR(200) NULL)
     */
    @Column(name = "ENDERECO", length = 200, nullable = true)
    private String endereco;

    /**
     * Número da residência/estabelecimento
     * Mapeado para a coluna 'NUMERO_CASA' (dba.sql: VARCHAR(10) NULL)
     */
    @Column(name = "NUMERO_CASA", length = 10, nullable = true)
    private String numeroCasa;

    /**
     * Código de Endereçamento Postal
     * Mapeado para a coluna 'CEP' (dba.sql: CHAR(8) NULL)
     */
    @Column(name = "CEP", length = 8, nullable = true)
    private String cep;

    /**
     * Nome do Município/Cidade
     * Mapeado para a coluna 'CIDADE' (dba.sql: VARCHAR(100) NULL)
     */
    @Column(name = "CIDADE", length = 100, nullable = true)
    private String cidade;

    /**
     * Unidade Federativa (Estado)
     * Mapeado para a coluna 'UF' (dba.sql: CHAR(2) NULL)
     */
    @Column(name = "UF", length = 2, nullable = true)
    private String uf;
}
