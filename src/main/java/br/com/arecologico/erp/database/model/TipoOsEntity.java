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
 * Entidade de Domínio / Tabela de Apoio para Tipos de Ordem de Serviço (OS).
 * Exemplos: 'Preventiva', 'Corretiva', 'Instalação', 'Higienização', 'Recarga de Gás'.
 * 
 * Anotações:
 * - @Entity: Mapeia como entidade JPA.
 * - @Table(name = "TIPO_OS"): Mapeia para a tabela TIPO_OS do dba.sql.
 * - @Id + @GeneratedValue(strategy = GenerationType.IDENTITY): Chave primária autoincrementável.
 * - @Column(name = "TIPO_OS", length = 50, nullable = false): Mapeia coluna VARCHAR(50) NOT NULL.
 * ============================================================================
 */
@Entity(name = "TipoOsEntity")
@Table(name = "TIPO_OS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoOsEntity {

    /**
     * Chave primária: COD_TIPO_OS INT NOT NULL IDENTITY(1,1)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_TIPO_OS")
    private Integer cod_Tipo_Os;

    /**
     * Descrição do Tipo de OS (dba.sql: TIPO_OS VARCHAR(50) NOT NULL)
     */
    @Column(name = "TIPO_OS", length = 50, nullable = false)
    private String tipo_Os;
}
