package br.com.arecologico.erp.database.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.arecologico.erp.database.model.PrestadorServicoEntity;

/**
 * ============================================================================
 * CONCEITOS SPRING DATA JPA (PDF - Slides 22, 28, 29 e 30):
 * ============================================================================
 * Repositório especializado para a subclasse PRESTADOR_SERVICO.
 * ============================================================================
 */
@Repository
public interface PrestadorServicoRepository extends JpaRepository<PrestadorServicoEntity, Integer> {

    /**
     * Consulta JPQL para buscar prestadores com valor de pagamento superior a um valor mínimo
     */
    @Query("SELECT p FROM PrestadorServicoEntity p WHERE p.pagamento >= :pagamentoMinimo ORDER BY p.pagamento DESC")
    List<PrestadorServicoEntity> buscarPorPagamentoMinimoJPQL(@Param("pagamentoMinimo") Float pagamentoMinimo);

    /**
     * Consulta SQL Nativa buscando técnicos/prestadores por cidade
     */
    @Query(value = "SELECT p.*, ps.PAGAMENTO FROM PESSOA p INNER JOIN PRESTADOR_SERVICO ps ON p.COD_PESSOA = ps.COD_PESSOA WHERE p.CIDADE = :cidade", nativeQuery = true)
    List<PrestadorServicoEntity> buscarPrestadoresPorCidadeNativo(@Param("cidade") String cidade);
}
