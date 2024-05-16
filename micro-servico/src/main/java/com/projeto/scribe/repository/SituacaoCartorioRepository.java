package com.projeto.scribe.repository;

import com.projeto.scribe.model.SituacaoCartorio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SituacaoCartorioRepository extends JpaRepository<SituacaoCartorio, String> {

    Page<SituacaoCartorio> findAll(Specification<SituacaoCartorio> tranSpec, Pageable pageable);

    boolean existsSituacaoCartorioByNome(String nome);

    boolean existsSituacaoCartorioById(String id);

    @Query(value = "SELECT id FROM projeto.situacao_cartorio WHERE nome = ?1", nativeQuery = true)
    String getIdSituacaoCartorio(String nome);

}
