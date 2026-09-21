package br.com.arecologico.erp.database.model;

import java.io.Serializable;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * CONCEITOS SPRING DATA JPA (PDF - Slides 17, 18, 19 e 20):
 * ============================================================================
 * Entidade Associativa representando a tabela 'CONTRATO_EQUIPAMENTO' (N:N).
 * 
 * dba.sql:
 * CREATE TABLE CONTRATO_EQUIPAMENTO (
 *     COD_CONTRATO     INT  NOT NULL,
 *     COD_EQUIPAMENTO  INT  NOT NULL,
 *     CONSTRAINT PK_CONTRATO_EQUIPAMENTO PRIMARY KEY (COD_CONTRATO, COD_EQUIPAMENTO),
 *     CONSTRAINT FK_CE_CONTRATO FOREIGN KEY (COD_CONTRATO) REFERENCES CONTRATO (COD_CONTRATO),
 *     CONSTRAINT FK_CE_EQUIPAMENTO FOREIGN KEY (COD_EQUIPAMENTO) REFERENCES EQUIPAMENTO (COD_EQUIPAMENTO)
 * );
 * 
 * Utiliza @EmbeddedId com a classe estática 'ContratoEquipamentoPK' (@Embeddable)
 * para representar de forma tipada e segura a Chave Primária Composta.
 * ============================================================================
 */
@Entity(name = "ContratoEquipamento")
@Table(name = "CONTRATO_EQUIPAMENTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContratoEquipamento {

    /**
     * Chave Primária Composta embutida (@EmbeddedId)
     */
    @EmbeddedId
    private ContratoEquipamentoPK id = new ContratoEquipamentoPK();

    /**
     * Mapeamento da Chave Estrangeira para CONTRATO
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codContrato")
    @JoinColumn(name = "COD_CONTRATO")
    private ContratoEntity contrato;

    /**
     * Mapeamento da Chave Estrangeira para EQUIPAMENTO
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codEquipamento")
    @JoinColumn(name = "COD_EQUIPAMENTO")
    private EquipamentoEntity equipamento;

    /**
     * Construtor auxiliar
     */
    public ContratoEquipamento(ContratoEntity contrato, EquipamentoEntity equipamento) {
        this.contrato = contrato;
        this.equipamento = equipamento;
        this.id = new ContratoEquipamentoPK(
            contrato != null ? contrato.getCod_Contrato() : null,
            equipamento != null ? equipamento.getCod_Equipamento() : null
        );
    }

    /**
     * Classe que representa a Chave Primária Composta (@Embeddable - Slide 19 do PDF)
     */
    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class ContratoEquipamentoPK implements Serializable {
        private Integer codContrato;
        private Integer codEquipamento;
    }
}
