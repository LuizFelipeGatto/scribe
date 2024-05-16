package com.projeto.scribe.model;

import com.projeto.scribe.util.ConfigUrl;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity(name = "atribuicao_cartorio")
@Table(schema = ConfigUrl.SCHEMA_PROJETO)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AtribuicaoCartorio {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column
    private String nome;

    @Column
    private Boolean situacao;

}
