package br.com.arecologico.erp.database.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.arecologico.erp.database.model.OSEntity;

/**
 * ============================================================================
 * CONCEITOS SPRING DATA JPA (PDF - Slide 22): Repositório para ORDEM_SERVICO
 * ============================================================================
 */
@Repository
public interface OSRepository extends JpaRepository<OSEntity, Integer> {

    /**
     * Consulta derivada por status da Ordem de Serviço
     */
    List<OSEntity> findByStatus(String status);

    /**
     * Consulta JPQL para buscar Ordens de Serviço abertas em um intervalo de datas
     */
    @Query("SELECT os FROM OSEntity os WHERE os.data_Abertura BETWEEN :dataInicio AND :dataFim ORDER BY os.data_Abertura DESC")
    List<OSEntity> buscarPorPeriodoAberturaJPQL(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);

    /**
     * Consulta JPQL com JOIN FETCH para carregar serviços vinculados à OS (Slide 17/22 do PDF)
     */
    @Query("SELECT DISTINCT os FROM OSEntity os LEFT JOIN FETCH os.servicos WHERE os.status = :status")
    List<OSEntity> buscarPorStatusComServicosJPQL(@Param("status") String status);

    /**
     * Consulta SQL Nativa para calcular o faturamento total de OS concluídas
     */
    @Query(value = "SELECT COALESCE(SUM(VALOR_TOTAL), 0) FROM ORDEM_SERVICO WHERE STATUS = 'Concluída'", nativeQuery = true)
    Double calcularFaturamentoTotalOSConcluidasNativo();
}
