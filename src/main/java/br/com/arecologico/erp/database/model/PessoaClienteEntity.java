package br.com.arecologico.erp.database.model;

import java.time.LocalDate;
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
 * CONCEITOS SPRING DATA JPA (PDF - Slides 24, 28, 29, 30 e 15):
 * ============================================================================
 * 1. HERANÇA COM @PrimaryKeyJoinColumn:
 *    - Subclasse na estratégia InheritanceType.JOINED.
 *    - @PrimaryKeyJoinColumn(name = "COD_PESSOA"): Especifica que a chave primária da
 *      tabela CLIENTE é ao mesmo tempo a Chave Primária e a Chave Estrangeira (FK) que
 *      faz referência à tabela 'PESSOA' (dba.sql: CONSTRAINT FK_CLIENTE_PESSOA).
 * 
 * 2. @Table(name = "CLIENTE"):
 *    - Mapeia para a tabela CLIENTE no banco de dados.
 * 
 * 3. @Column:
 *    - EMAIL VARCHAR(150) NULL
 *    - TIPO CHAR(1) NOT NULL ('F' = Física / 'J' = Jurídica)
 *    - DATA_NASCIMENTO DATE NULL
 * 
 * 4. @OneToMany:
 *    - Relacionamento 1:N com ContratoEntity e OSEntity.
 * ============================================================================
 */
@Entity(name = "PessoaClienteEntity")
@Table(name = "CLIENTE")
@PrimaryKeyJoinColumn(name = "COD_PESSOA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PessoaClienteEntity extends PessoaEntity {

    /**
     * E-mail do cliente (dba.sql: EMAIL VARCHAR(150) NULL)
     */
    @Column(name = "EMAIL", length = 150, nullable = true)
    private String email;

    /**
     * Tipo do cliente: 'F' = Pessoa Física, 'J' = Pessoa Jurídica
     * dba.sql: TIPO CHAR(1) NOT NULL (CONSTRAINT CHK_CLIENTE_TIPO CHECK (TIPO IN ('F', 'J')))
     */
    @Column(name = "TIPO", length = 1, nullable = false)
    private String tipo;

    /**
     * Data de nascimento para PF ou fundação para PJ
     * dba.sql: DATA_NASCIMENTO DATE NULL
     */
    @Column(name = "DATA_NASCIMENTO", nullable = true)
    private LocalDate data_Nascimento;

    /**
     * Lista de contratos vinculados a este cliente
     */
    @JsonIgnore
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ContratoEntity> contratos = new ArrayList<>();

    /**
     * Lista de Ordens de Serviço abertas para este cliente
     */
    @JsonIgnore
    @OneToMany(mappedBy = "pessoa_Cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OSEntity> ordensServico = new ArrayList<>();

    /**
     * Construtor completo herdando da superclasse PessoaEntity
     */
    public PessoaClienteEntity(Integer cod_Pessoa, String nome_Pessoa, String CPF, String RG,
                               String telefone, String CEP, String estado, String cidade,
                               String endereco, String numero_Casa, String email, String tipo,
                               LocalDate data_Nascimento) {
        super(cod_Pessoa, nome_Pessoa, CPF, RG, telefone, CEP, estado, cidade, endereco, numero_Casa);
        this.email = email;
        this.tipo = tipo;
        this.data_Nascimento = data_Nascimento;
        this.contratos = new ArrayList<>();
        this.ordensServico = new ArrayList<>();
    }
}
