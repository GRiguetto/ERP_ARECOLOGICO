package br.com.arecologico.erp.database.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * CONCEITOS SPRING DATA JPA (PDF - Slides 3, 4, 5, 6, 7 e 17):
 * ============================================================================
 * 1. @Entity: Marca como entidade persistível JPA.
 * 
 * 2. @Table: Mapeia para a tabela 'SERVICO' do script dba.sql.
 * 
 * 3. @Id + @GeneratedValue(strategy = GenerationType.IDENTITY): Chave primária autoincrementada.
 * 
 * 4. @Column:
 *    - precision = 10, scale = 2: Configuração específica do PDF para campos monetários/decimais
 *      (DECIMAL(10,2) no dba.sql).
 *    - length = 300, nullable = false: Mapeamento de constraints VARCHAR(300) NOT NULL.
 * 
 * 5. @ManyToMany(mappedBy = "servicos"): Relacionamento N:N com OSEntity (Ordem de Serviço).
 *    Um serviço pode estar presente em várias Ordens de Serviço.
 * ============================================================================
 */
@Entity(name = "ServicoEntity")
@Table(name = "SERVICO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServicoEntity {

    /**
     * Chave primária: COD_SERVICO INT NOT NULL IDENTITY(1,1)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_SERVICO")
    private Integer cod_Servico;

    /**
     * Descrição detalhada do serviço prestado
     * dba.sql: DESCRICAO_SERVICO VARCHAR(300) NOT NULL
     */
    @Column(name = "DESCRICAO_SERVICO", length = 300, nullable = false)
    private String descricao;

    /**
     * Valor cobrado pelo serviço (DECIMAL(10,2) NOT NULL)
     * Usando precision=10 e scale=2 conforme ensinado no slide 7 do PDF.
     */
    @Column(name = "VALOR_SERVICO", precision = 10, scale = 2, nullable = false)
    private Float valor_Servico;

    /**
     * Data de vigência inicial do serviço (dba.sql: DATA_SERVICO_INICIO DATE NULL)
     */
    @Column(name = "DATA_SERVICO_INICIO")
    private LocalDate data_Ser_Inicio;

    /**
     * Data de vigência final do serviço (dba.sql: DATA_SERVICO_FIM DATE NULL)
     */
    @Column(name = "DATA_SERVICO_FIM")
    private LocalDate data_Ser_Fim;

    /**
     * Lista de Ordens de Serviço associadas a este serviço (N:N via ORDEM_SERVICO_SERVICO)
     */
    @JsonIgnore
    @ManyToMany(mappedBy = "servicos", fetch = FetchType.LAZY)
    private List<OSEntity> ordensServico = new ArrayList<>();

    /**
     * Construtor de conveniência para inicialização rápida sem lista de OSs
     */
    public ServicoEntity(Integer cod_Servico, String descricao, Float valor_Servico, LocalDate data_Ser_Inicio, LocalDate data_Ser_Fim) {
        this.cod_Servico = cod_Servico;
        this.descricao = descricao;
        this.valor_Servico = valor_Servico;
        this.data_Ser_Inicio = data_Ser_Inicio;
        this.data_Ser_Fim = data_Ser_Fim;
        this.ordensServico = new ArrayList<>();
    }
}
