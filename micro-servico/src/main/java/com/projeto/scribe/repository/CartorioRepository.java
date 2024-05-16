package com.projeto.scribe.repository;

import com.projeto.scribe.model.Cartorio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartorioRepository extends JpaRepository<Cartorio, Integer> {

    Page<Cartorio> findAll(Specification<Cartorio> tranSpec, Pageable pageable);

    boolean existsCartorioByNome(String nome);

    boolean existsCartorioById(Integer id);

    @Query(value = "SELECT id FROM projeto.cartorio WHERE nome = ?1", nativeQuery = true)
    String getIdCartorio(String nome);

    @Query(value = "SELECT EXISTS(SELECT * FROM projeto.cartorio WHERE situacao_cartorio LIKE ?1)", nativeQuery = true)
    Boolean isSituacaoVinculada(String id);

    @Query(value = "SELECT EXISTS(SELECT * FROM projeto.rel_cartorio_atribuicao WHERE cartorio = ?1)", nativeQuery = true)
    Boolean isCartorioVinculado(Integer id);
}
