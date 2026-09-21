package br.com.arecologico.erp.database.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * CONCEITOS SPRING DATA JPA (PDF - Slides 3, 4, 5, 6 e 7):
 * ============================================================================
 * Entidade de Domínio / Tabela de Apoio para Tipos de Contrato.
 * Exemplos: 'Mensal', 'Trimestral', 'Semestral', 'Anual', 'Preventivo Plus'.
 * 
 * Anotações:
 * - @Entity: Mapeia como entidade JPA.
 * - @Table(name = "TIPO_CONTRATO"): Mapeia para a tabela TIPO_CONTRATO do dba.sql.
 * - @Id + @GeneratedValue(strategy = GenerationType.IDENTITY): Chave primária autoincrementável.
 * - @Column(name = "TIPO_CONTRATO", length = 50, nullable = false): Configura a coluna VARCHAR(50) NOT NULL.
 * ============================================================================
 */
@Entity(name = "TipoContratoEntity")
@Table(name = "TIPO_CONTRATO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoContratoEntity {

    /**
     * Chave primária: COD_TIPO_CONTRATO INT NOT NULL IDENTITY(1,1)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_TIPO_CONTRATO")
    private Integer cod_Tipo_Contrato;

    /**
     * Descrição do Tipo de Contrato (dba.sql: TIPO_CONTRATO VARCHAR(50) NOT NULL)
     */
    @Column(name = "TIPO_CONTRATO", length = 50, nullable = false)
    private String tipo_Contrato;
}
