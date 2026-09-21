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
 * Entidade Associativa representando a tabela 'CONTRATO_OS' (N:N).
 * 
 * dba.sql:
 * CREATE TABLE CONTRATO_OS (
 *     COD_CONTRATO  INT  NOT NULL,
 *     COD_OS        INT  NOT NULL,
 *     CONSTRAINT PK_CONTRATO_OS PRIMARY KEY (COD_CONTRATO, COD_OS),
 *     CONSTRAINT FK_COS_CONTRATO FOREIGN KEY (COD_CONTRATO) REFERENCES CONTRATO (COD_CONTRATO),
 *     CONSTRAINT FK_COS_OS FOREIGN KEY (COD_OS) REFERENCES ORDEM_SERVICO (COD_OS)
 * );
 * 
 * Utiliza @EmbeddedId e @Embeddable para a chave composta.
 * ============================================================================
 */
@Entity(name = "ContratoOS")
@Table(name = "CONTRATO_OS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContratoOS {

    /**
     * Chave Primária Composta embutida (@EmbeddedId)
     */
    @EmbeddedId
    private ContratoOSPK id = new ContratoOSPK();

    /**
     * Mapeamento N:1 para a entidade CONTRATO
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codContrato")
    @JoinColumn(name = "COD_CONTRATO")
    private ContratoEntity contrato;

    /**
     * Mapeamento N:1 para a entidade ORDEM_SERVICO
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codOs")
    @JoinColumn(name = "COD_OS")
    private OSEntity os;

    /**
     * Construtor auxiliar
     */
    public ContratoOS(ContratoEntity contrato, OSEntity os) {
        this.contrato = contrato;
        this.os = os;
        this.id = new ContratoOSPK(
            contrato != null ? contrato.getCod_Contrato() : null,
            os != null ? os.getCod_Os() : null
        );
    }

    /**
     * Classe para a Chave Primária Composta (@Embeddable)
     */
    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class ContratoOSPK implements Serializable {
        private Integer codContrato;
        private Integer codOs;
    }
}
