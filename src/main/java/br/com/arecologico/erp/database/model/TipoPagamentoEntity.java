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
 * Entidade de Domínio / Tabela de Apoio para Formas de Pagamento.
 * Exemplos: 'Pix', 'Cartão de Crédito', 'Boleto Bancário', 'Transferência'.
 * 
 * Anotações:
 * - @Entity: Mapeia como entidade JPA.
 * - @Table(name = "TIPO_PAGAMENTO"): Mapeia para a tabela TIPO_PAGAMENTO do dba.sql.
 * - @Id + @GeneratedValue(strategy = GenerationType.IDENTITY): Chave primária autoincrementável.
 * - @Column(name = "TIPO_PAGAMENTO", length = 50, nullable = false): Mapeia coluna VARCHAR(50) NOT NULL.
 * ============================================================================
 */
@Entity(name = "TipoPagamentoEntity")
@Table(name = "TIPO_PAGAMENTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoPagamentoEntity {

    /**
     * Chave primária: COD_TIPO_PAGAMENTO INT NOT NULL IDENTITY(1,1)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_TIPO_PAGAMENTO")
    private Integer cod_Tipo_Pagamento;

    /**
     * Descrição da forma de pagamento (dba.sql: TIPO_PAGAMENTO VARCHAR(50) NOT NULL)
     */
    @Column(name = "TIPO_PAGAMENTO", length = 50, nullable = false)
    private String tipo_Pagamento;
}
