package com.projeto.scribe.dto;

import com.projeto.scribe.model.AtribuicaoCartorio;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtribuicaoCartorioDTO {

    private String nome;

    private Boolean situacao;

    public AtribuicaoCartorioDTO(AtribuicaoCartorio atribuicaoCartorio){
        this.nome = atribuicaoCartorio.getNome();
        this.situacao = atribuicaoCartorio.getSituacao();
    }

}
