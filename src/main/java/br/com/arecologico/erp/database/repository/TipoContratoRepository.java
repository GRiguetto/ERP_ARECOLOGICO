package br.com.arecologico.erp.database.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.arecologico.erp.database.model.TipoContratoEntity;

/**
 * ============================================================================
 * CONCEITOS SPRING DATA JPA (PDF - Slide 22): Repositório para TIPO_CONTRATO
 * ============================================================================
 */
@Repository
public interface TipoContratoRepository extends JpaRepository<TipoContratoEntity, Integer> {

    /**
     * Consulta derivada para buscar pelo tipo de contrato
     */
    Optional<TipoContratoEntity> findByTipo_Contrato(String tipoContrato);

    /**
     * Consulta JPQL personalizada
     */
    @Query("SELECT t FROM TipoContratoEntity t WHERE LOWER(t.tipo_Contrato) = LOWER(:tipo)")
    Optional<TipoContratoEntity> buscarPorDescricaoJPQL(@Param("tipo") String tipo);
}
