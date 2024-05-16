package com.projeto.scribe.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultadoDTO<T> {

    private boolean sucesso;
    private T resultado;
    private String mensagemErro;

}
