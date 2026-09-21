package br.com.arecologico.erp.database.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * CONCEITOS SPRING DATA JPA (PDF - Slides 24, 28, 29, 30 e 7):
 * ============================================================================
 * 1. HERANÇA COM @PrimaryKeyJoinColumn:
 *    - Subclasse de PessoaEntity na estratégia JOINED.
 *    - A tabela 'PRESTADOR_SERVICO' utiliza COD_PESSOA tanto como Chave Primária
 *      quanto Chave Estrangeira apontando para PESSOA(COD_PESSOA).
 * 
 * 2. @Column:
 *    - PAGAMENTO: Mapeado com precision=10 e scale=2 (DECIMAL(10,2) no dba.sql).
 * 
 * 3. @OneToMany:
 *    - Relacionamento 1:N com as Ordens de Serviço executadas por este prestador/técnico.
 * ============================================================================
 */
@Entity(name = "PrestadorServicoEntity")
@Table(name = "PRESTADOR_SERVICO")
@PrimaryKeyJoinColumn(name = "COD_PESSOA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrestadorServicoEntity extends PessoaEntity {

    /**
     * Valor fixo ou base de pagamento do prestador/técnico
     * dba.sql: PAGAMENTO DECIMAL(10,2) NULL
     */
    @Column(name = "PAGAMENTO", precision = 10, scale = 2, nullable = true)
    private Float pagamento;

    /**
     * Lista de Ordens de Serviço atendidas por este prestador
     */
    @JsonIgnore
    @OneToMany(mappedBy = "prestador_Servico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OSEntity> ordensServico = new ArrayList<>();

    /**
     * Construtor completo repassando dados para superclasse PessoaEntity
     */
    public PrestadorServicoEntity(Integer cod_Pessoa, String nome_Pessoa, String CPF, String RG,
                                  String telefone, String CEP, String estado, String cidade,
                                  String endereco, String numero_Casa, Float pagamento) {
        super(cod_Pessoa, nome_Pessoa, CPF, RG, telefone, CEP, estado, cidade, endereco, numero_Casa);
        this.pagamento = pagamento;
        this.ordensServico = new ArrayList<>();
    }
}
