package br.com.arecologico.erp.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.arecologico.erp.database.model.PessoaClienteEntity;

/**
 * ============================================================================
 * CONCEITOS SPRING DATA JPA (PDF - Slides 22, 28, 29 e 30):
 * ============================================================================
 * Repositório especializado para a subclasse CLIENTE (PessoaClienteEntity).
 * O JPA executa automaticamente o INNER JOIN entre 'CLIENTE' e 'PESSOA'.
 * ============================================================================
 */
@Repository
public interface PessoaClienteRepository extends JpaRepository<PessoaClienteEntity, Integer> {

    /**
     * Busca cliente por e-mail
     */
    Optional<PessoaClienteEntity> findByEmail(String email);

    /**
     * Busca clientes por tipo ('F' = Física ou 'J' = Jurídica)
     */
    List<PessoaClienteEntity> findByTipo(String tipo);

    /**
     * Consulta JPQL unindo dados da classe filha e da superclasse
     */
    @Query("SELECT c FROM PessoaClienteEntity c WHERE c.tipo = :tipo AND LOWER(c.dadosEndereco.uf) = LOWER(:uf)")
    List<PessoaClienteEntity> buscarPorTipoEUfJPQL(@Param("tipo") String tipo, @Param("uf") String uf);

    /**
     * Consulta SQL Nativa na tabela CLIENTE unida à tabela PESSOA
     */
    @Query(value = "SELECT p.*, c.EMAIL, c.TIPO, c.DATA_NASCIMENTO FROM PESSOA p INNER JOIN CLIENTE c ON p.COD_PESSOA = c.COD_PESSOA WHERE c.TIPO = 'J'", nativeQuery = true)
    List<PessoaClienteEntity> buscarApenasPessoasJuridicasNativo();
}
