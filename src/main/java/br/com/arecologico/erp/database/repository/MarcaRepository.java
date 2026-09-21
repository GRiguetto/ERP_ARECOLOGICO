package br.com.arecologico.erp.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.arecologico.erp.database.model.MarcaEntity;

/**
 * ============================================================================
 * CONCEITOS SPRING DATA JPA (PDF - Slide 22): @Repository e @Query
 * ============================================================================
 * 1. @Repository:
 *    - Marca a interface como um componente de acesso a dados (DAO/Repository) do Spring.
 *    - Habilita a tradução automática de exceções de banco de dados para a hierarquia do Spring.
 * 
 * 2. JpaRepository<MarcaEntity, Integer>:
 *    - Fornece métodos CRUD prontos para uso (save, findById, findAll, deleteById, count, etc.).
 * 
 * 3. @Query com JPQL (Java Persistence Query Language):
 *    - Permite escrever consultas orientadas a objetos, referenciando entidades e atributos Java.
 * 
 * 4. @Query com nativeQuery = true:
 *    - Permite executar comandos SQL nativos específicos do banco de dados relacional.
 * ============================================================================
 */
@Repository
public interface MarcaRepository extends JpaRepository<MarcaEntity, Integer> {

    /**
     * Consulta derivada por convenção de nomenclatura (Spring Data Query Methods)
     */
    Optional<MarcaEntity> findByNome_Marca(String nomeMarca);

    /**
     * Consulta personalizada usando JPQL (@Query - Slide 22 do PDF)
     * Pesquisa marcas que contenham determinado texto no nome (Case-insensitive)
     */
    @Query("SELECT m FROM MarcaEntity m WHERE LOWER(m.nome_Marca) LIKE LOWER(CONCAT('%', :termo, '%'))")
    List<MarcaEntity> buscarPorNomeContendoJPQL(@Param("termo") String termo);

    /**
     * Consulta personalizada usando SQL NATIVO (@Query com nativeQuery = true - Slide 22 do PDF)
     */
    @Query(value = "SELECT * FROM MARCA WHERE NOME_MARCA = :nome", nativeQuery = true)
    Optional<MarcaEntity> buscarPorNomeExatoSQLNativo(@Param("nome") String nome);
}
