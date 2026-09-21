package br.com.arecologico.erp.database.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.arecologico.erp.database.model.PessoaEntity;

/**
 * ============================================================================
 * CONCEITOS SPRING DATA JPA (PDF - Slides 22, 24, 28 e 30):
 * ============================================================================
 * Repositório base para a hierarquia polimórfica de PESSOA.
 * Por utilizar a estratégia InheritanceType.JOINED, uma consulta nesta interface
 * é capaz de retornar instâncias tanto de PessoaEntity, PessoaClienteEntity quanto
 * de PrestadorServicoEntity polimorficamente.
 * ============================================================================
 */
@Repository
public interface PessoaRepository extends JpaRepository<PessoaEntity, Integer> {

    /**
     * Consulta derivada: busca pessoa pelo CPF
     */
    Optional<PessoaEntity> findByCPF(String cpf);

    /**
     * Consulta JPQL navegando no objeto embutido @Embedded 'dadosEndereco' (Slide 19-20 do PDF)
     */
    @Query("SELECT p FROM PessoaEntity p WHERE LOWER(p.dadosEndereco.cidade) = LOWER(:cidade)")
    List<PessoaEntity> buscarPorCidadeJPQL(@Param("cidade") String cidade);

    /**
     * Consulta SQL Nativa para buscar por parte do nome
     */
    @Query(value = "SELECT * FROM PESSOA WHERE NOME LIKE %:nome%", nativeQuery = true)
    List<PessoaEntity> buscarPorNomeNativo(@Param("nome") String nome);
}
