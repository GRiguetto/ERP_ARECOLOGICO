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
 * Entidade Associativa representando a tabela 'ORDEM_SERVICO_SERVICO' (N:N).
 * 
 * dba.sql:
 * CREATE TABLE ORDEM_SERVICO_SERVICO (
 *     COD_OS       INT  NOT NULL,
 *     COD_SERVICO  INT  NOT NULL,
 *     CONSTRAINT PK_OS_SERVICO PRIMARY KEY (COD_OS, COD_SERVICO),
 *     CONSTRAINT FK_OSS_OS FOREIGN KEY (COD_OS) REFERENCES ORDEM_SERVICO (COD_OS),
 *     CONSTRAINT FK_OSS_SERVICO FOREIGN KEY (COD_SERVICO) REFERENCES SERVICO (COD_SERVICO)
 * );
 * 
 * Demonstração de @Embeddable, @EmbeddedId, @ManyToOne e @MapsId.
 * ============================================================================
 */
@Entity(name = "OrdemServicoServico")
@Table(name = "ORDEM_SERVICO_SERVICO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdemServicoServico {

    /**
     * Chave Primária Composta (@EmbeddedId)
     */
    @EmbeddedId
    private OrdemServicoServicoPK id = new OrdemServicoServicoPK();

    /**
     * Mapeamento para a ORDEM_SERVICO
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codOs")
    @JoinColumn(name = "COD_OS")
    private OSEntity os;

    /**
     * Mapeamento para o SERVICO
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("codServico")
    @JoinColumn(name = "COD_SERVICO")
    private ServicoEntity servico;

    /**
     * Construtor auxiliar
     */
    public OrdemServicoServico(OSEntity os, ServicoEntity servico) {
        this.os = os;
        this.servico = servico;
        this.id = new OrdemServicoServicoPK(
            os != null ? os.getCod_Os() : null,
            servico != null ? servico.getCod_Servico() : null
        );
    }

    /**
     * Classe da Chave Composta (@Embeddable)
     */
    @Embeddable
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class OrdemServicoServicoPK implements Serializable {
        private Integer codOs;
        private Integer codServico;
    }
}
