package com.projeto.scribe.specification;

import com.projeto.scribe.model.Cartorio;
import com.projeto.scribe.specification.filter.Filtro;

import java.util.Objects;

public class CartorioSpecification extends SpecificationBuilder<Cartorio, Filtro>{

    @Override
    protected void loadPredicates(Filtro filtro) {

        withFiltro(filtro);

    }

    private void withFiltro(Filtro filtro) {

        if(Objects.nonNull(filtro.getId())) {
            addPredicates(cb.equal(root.get("id"), filtro.getId()));
        }

        if(Objects.nonNull(filtro.getNome())) {
            addPredicates(cb.equal(root.get("nome"), filtro.getNome()));
        }

    }

}
