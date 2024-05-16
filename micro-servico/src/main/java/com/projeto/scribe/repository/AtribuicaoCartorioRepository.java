package com.projeto.scribe.repository;

import com.projeto.scribe.model.AtribuicaoCartorio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface AtribuicaoCartorioRepository extends JpaRepository<AtribuicaoCartorio, String> {

    Page<AtribuicaoCartorio> findAll(Specification<AtribuicaoCartorio> tranSpec, Pageable pageable);

    boolean existsAtribuicaoCartorioByNome(String nome);

    boolean existsAtribuicaoCartorioById(String id);

    @Query(value = "SELECT id FROM projeto.atribuicao_cartorio WHERE nome = ?1", nativeQuery = true)
    String getIdAtribuicao(String nome);

    @Query(value = "SELECT EXISTS(SELECT * FROM projeto.rel_cartorio_atribuicao WHERE atribuicao LIKE ?1)", nativeQuery = true)
    Boolean isAtribuicaoVinculada(String id);

}
