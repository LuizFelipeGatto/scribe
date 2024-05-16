package com.projeto.scribe.specification.filter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@Setter
public class Filtro implements IFilter{

    private String id;
    private String nome;
    private String campoSimplificado = "";

    private Boolean simplificado = false;
    private Integer pagina = 0;
    private Integer tamanhoPagina = 10;
    private String ordem = "id";
    private List<String> ordemList;
    private Sort.Direction ordemDirecao = Sort.Direction.DESC;

}
