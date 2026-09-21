package br.com.arecologico.erp.database.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.arecologico.erp.database.model.EquipamentoEntity;

/**
 * ============================================================================
 * CONCEITOS SPRING DATA JPA (PDF - Slide 22): Repositório para EQUIPAMENTO
 * ============================================================================
 */
@Repository
public interface EquipamentoRepository extends JpaRepository<EquipamentoEntity, Integer> {

    /**
     * Consulta derivada: busca equipamentos pela capacidade térmica (BTUs)
     */
    List<EquipamentoEntity> findByBtus(Integer btus);

    /**
     * Consulta JPQL com JOIN (@Query - Slide 22 do PDF)
     * Busca equipamentos pelo nome da marca associada
     */
    @Query("SELECT e FROM EquipamentoEntity e JOIN e.marca m WHERE LOWER(m.nome_Marca) = LOWER(:nomeMarca)")
    List<EquipamentoEntity> buscarPorNomeMarcaJPQL(@Param("nomeMarca") String nomeMarca);

    /**
     * Consulta em SQL NATIVO (@Query com nativeQuery = true - Slide 22 do PDF)
     * Busca equipamentos com capacidade igual ou superior a determinada quantidade de BTUs
     */
    @Query(value = "SELECT * FROM EQUIPAMENTO WHERE BTUS >= :btusMinimo ORDER BY BTUS ASC", nativeQuery = true)
    List<EquipamentoEntity> buscarEquipamentosDeAltaCapacidadeNativo(@Param("btusMinimo") Integer btusMinimo);
}
