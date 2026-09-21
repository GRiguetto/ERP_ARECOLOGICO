package br.com.arecologico.erp.database.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.arecologico.erp.database.model.TipoOsEntity;

/**
 * ============================================================================
 * CONCEITOS SPRING DATA JPA (PDF - Slide 22): Repositório para TIPO_OS
 * ============================================================================
 */
@Repository
public interface TipoOsRepository extends JpaRepository<TipoOsEntity, Integer> {

    /**
     * Consulta derivada para buscar pelo tipo de OS
     */
    Optional<TipoOsEntity> findByTipo_Os(String tipoOs);

    /**
     * Consulta JPQL personalizada
     */
    @Query("SELECT t FROM TipoOsEntity t WHERE LOWER(t.tipo_Os) = LOWER(:tipo)")
    Optional<TipoOsEntity> buscarPorTipoJPQL(@Param("tipo") String tipo);
}
