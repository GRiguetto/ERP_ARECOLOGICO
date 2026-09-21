package br.com.arecologico.erp.database.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * CONCEITOS SPRING DATA JPA (PDF - Slides 3, 4, 5, 6, 7 e 15):
 * ============================================================================
 * 1. @Entity: Marca esta classe Java como uma entidade gerenciada pelo JPA/Hibernate.
 *    Parâmetro 'name': Define o nome da entidade na JPQL (opcional, padrão é o nome da classe).
 * 
 * 2. @Table: Especifica a tabela correspondente no banco de dados relacional.
 *    Parâmetro 'name': "MARCA", conforme definido no script dba.sql.
 * 
 * 3. @Id: Especifica que o atributo 'codMarca' é a Chave Primária (Primary Key) da entidade.
 * 
 * 4. @GeneratedValue: Define a estratégia de auto-geração do identificador no banco.
 *    strategy = GenerationType.IDENTITY: Compatível com colunas IDENTITY do SQL Server/MySQL.
 * 
 * 5. @Column: Especifica o mapeamento detalhado da coluna:
 *    - name: Nome exato da coluna no banco ("COD_MARCA", "NOME_MARCA")
 *    - length: Tamanho máximo permitido (100 caracteres)
 *    - nullable: false (coluna NOT NULL)
 * 
 * 6. @OneToMany: Relacionamento 1:N (Uma marca possui muitos equipamentos).
 *    - mappedBy: Nome do atributo na classe 'EquipamentoEntity' que é dono do relacionamento.
 *    - cascade: Operações em cascata (PERSIST, MERGE, etc.).
 *    - fetch: FetchType.LAZY para evitar carregamento desnecessário de listas da memória.
 * ============================================================================
 */
@Entity(name = "MarcaEntity")
@Table(name = "MARCA")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MarcaEntity {

    /**
     * Chave primária da tabela MARCA (COD_MARCA INT NOT NULL IDENTITY(1,1))
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_MARCA")
    private Integer cod_Marca;

    /**
     * Nome da Marca de Climatização (Ex: LG, Samsung, Elgin, Daikin, Midea)
     * dba.sql: NOME_MARCA VARCHAR(100) NOT NULL
     */
    @Column(name = "NOME_MARCA", length = 100, nullable = false)
    private String nome_Marca;

    /**
     * Lista de equipamentos vinculados a esta marca (Relacionamento bidirecional 1:N)
     * @JsonIgnore evita loops de serialização JSON cíclica caso seja serializado diretamente.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EquipamentoEntity> equipamentos = new ArrayList<>();

    /**
     * Construtor de conveniência para inicialização rápida sem lista de equipamentos
     * @param cod_Marca Identificador da marca
     * @param nome_Marca Nome da marca
     */
    public MarcaEntity(Integer cod_Marca, String nome_Marca) {
        this.cod_Marca = cod_Marca;
        this.nome_Marca = nome_Marca;
        this.equipamentos = new ArrayList<>();
    }
}
