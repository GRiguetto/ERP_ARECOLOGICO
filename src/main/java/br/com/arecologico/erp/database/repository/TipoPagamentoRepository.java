package br.com.arecologico.erp.database.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.arecologico.erp.database.model.TipoPagamentoEntity;

/**
 * ============================================================================
 * CONCEITOS SPRING DATA JPA (PDF - Slide 22): Repositório para TIPO_PAGAMENTO
 * ============================================================================
 */
@Repository
public interface TipoPagamentoRepository extends JpaRepository<TipoPagamentoEntity, Integer> {

    /**
     * Consulta derivada para buscar pelo nome da forma de pagamento
     */
    Optional<TipoPagamentoEntity> findByTipo_Pagamento(String tipoPagamento);

    /**
     * Consulta SQL Nativo (@Query com nativeQuery = true - Slide 22 do PDF)
     */
    @Query(value = "SELECT * FROM TIPO_PAGAMENTO WHERE UPPER(TIPO_PAGAMENTO) = UPPER(:tipo)", nativeQuery = true)
    Optional<TipoPagamentoEntity> buscarPorTipoNativo(@Param("tipo") String tipo);
}
