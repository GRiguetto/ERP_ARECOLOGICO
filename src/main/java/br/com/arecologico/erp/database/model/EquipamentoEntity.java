package br.com.arecologico.erp.database.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ============================================================================
 * CONCEITOS SPRING DATA JPA (PDF - Slides 3, 4, 5, 6, 7, 11, 17 e 18):
 * ============================================================================
 * 1. @Entity: Define que a classe é uma entidade persistível no banco.
 * 
 * 2. @Table: Vincula a classe à tabela 'EQUIPAMENTO'.
 * 
 * 3. @ManyToOne: Configura o relacionamento N:1 (Vários equipamentos pertencem a uma Marca).
 *    - fetch = FetchType.LAZY: Carrega a entidade associada apenas sob demanda (boa prática de performance).
 *    - optional = false: Indica que o relacionamento é obrigatório (não pode haver equipamento sem marca).
 * 
 * 4. @JoinColumn: Especifica a coluna de Chave Estrangeira (FK) na tabela EQUIPAMENTO:
 *    - name = "COD_MARCA": Nome da coluna FK no banco de dados.
 *    - referencedColumnName = "COD_MARCA": Coluna referenciada na tabela MARCA.
 *    - nullable = false: Restrição de não nulidade no banco.
 * 
 * 5. @ManyToMany(mappedBy = "equipamentos"): Relacionamento N:N com ContratoEntity (lado não-proprietário).
 * ============================================================================
 */
@Entity(name = "EquipamentoEntity")
@Table(name = "EQUIPAMENTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EquipamentoEntity {

    /**
     * Chave primária: COD_EQUIPAMENTO INT NOT NULL IDENTITY(1,1)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_EQUIPAMENTO")
    private Integer cod_Equipamento;

    /**
     * Modelo do equipamento (ex: 'Split Hi-Wall S4NQ09WA3', 'WindFree AR09')
     * dba.sql: MODELO VARCHAR(100) NOT NULL
     */
    @Column(name = "MODELO", length = 100, nullable = false)
    private String modelo;

    /**
     * Capacidade térmica em BTUs (ex: 9000, 12000, 18000, 24000)
     * dba.sql: BTUS INT NOT NULL
     */
    @Column(name = "BTUS", nullable = false)
    private Integer btus;

    /**
     * Marca associada (FK -> MARCA.COD_MARCA)
     * Mapeamento N:1 com Fetch LAZY para otimização de consultas
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "COD_MARCA", referencedColumnName = "COD_MARCA", nullable = false)
    private MarcaEntity marca;

    /**
     * Lista de contratos aos quais este equipamento está vinculado (N:N com CONTRATO)
     */
    @JsonIgnore
    @ManyToMany(mappedBy = "equipamentos", fetch = FetchType.LAZY)
    private List<ContratoEntity> contratos = new ArrayList<>();

    /**
     * Construtor de conveniência para inicialização sem lista de contratos
     */
    public EquipamentoEntity(Integer cod_Equipamento, String modelo, Integer btus, MarcaEntity marca) {
        this.cod_Equipamento = cod_Equipamento;
        this.modelo = modelo;
        this.btus = btus;
        this.marca = marca;
        this.contratos = new ArrayList<>();
    }
}
