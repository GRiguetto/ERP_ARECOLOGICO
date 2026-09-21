package br.com.arecologico.erp.database.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.arecologico.erp.database.model.ContratoEntity;

/**
 * ============================================================================
 * CONCEITOS SPRING DATA JPA (PDF - Slide 22): Repositório para CONTRATO
 * ============================================================================
 */
@Repository
public interface ContratoRepository extends JpaRepository<ContratoEntity, Integer> {

    /**
     * Consulta JPQL para buscar contratos com vigência ativa em determinada data
     */
    @Query("SELECT c FROM ContratoEntity c WHERE :dataAtual BETWEEN c.data_Con_Inicio AND c.data_Con_Fim")
    List<ContratoEntity> buscarContratosVigentesJPQL(@Param("dataAtual") LocalDate dataAtual);

    /**
     * Consulta JPQL com JOIN FETCH para carregar contratos com seus equipamentos
     * evitando o problema clássico de N+1 consultas (Slide 15/17 do PDF)
     */
    @Query("SELECT DISTINCT c FROM ContratoEntity c LEFT JOIN FETCH c.equipamentos WHERE c.valor_Contrato >= :valorMinimo")
    List<ContratoEntity> buscarContratosComEquipamentosJPQL(@Param("valorMinimo") Float valorMinimo);

    /**
     * Consulta SQL Nativa para listar contratos de um cliente específico
     */
    @Query(value = "SELECT * FROM CONTRATO WHERE COD_PESSOA = :codCliente ORDER BY DATA_CONTRATO_INICIO DESC", nativeQuery = true)
    List<ContratoEntity> buscarPorClienteIdNativo(@Param("codCliente") Integer codCliente);
}
