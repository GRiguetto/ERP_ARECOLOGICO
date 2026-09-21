package br.com.arecologico.erp.database.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.arecologico.erp.database.model.ServicoEntity;

/**
 * ============================================================================
 * CONCEITOS SPRING DATA JPA (PDF - Slide 22): Repositório para SERVICO
 * ============================================================================
 */
@Repository
public interface ServicoRepository extends JpaRepository<ServicoEntity, Integer> {

    /**
     * Consulta JPQL para buscar serviços com valor menor ou igual a determinado teto
     */
    @Query("SELECT s FROM ServicoEntity s WHERE s.valor_Servico <= :valorMaximo ORDER BY s.valor_Servico ASC")
    List<ServicoEntity> buscarServicosPorFaixaDePrecoJPQL(@Param("valorMaximo") Float valorMaximo);

    /**
     * Consulta SQL Nativa (@Query nativeQuery = true - Slide 22 do PDF)
     * Busca serviços ativos (sem data de término definida)
     */
    @Query(value = "SELECT * FROM SERVICO WHERE DATA_SERVICO_FIM IS NULL", nativeQuery = true)
    List<ServicoEntity> buscarServicosAtivosNativo();
}
