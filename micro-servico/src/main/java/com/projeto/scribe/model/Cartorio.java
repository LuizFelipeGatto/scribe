package com.projeto.scribe.model;

import com.projeto.scribe.util.ConfigUrl;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity(name = "cartorio")
@Table(schema = ConfigUrl.SCHEMA_PROJETO, name = "cartorio")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cartorio {

    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "cartorio_id_seq")
    @SequenceGenerator(name = "cartorio_id_seq", sequenceName = ConfigUrl.SCHEMA_PROJETO + ".cartorio_id_seq", allocationSize=1)
    private Integer id;

    @Column
    private String nome;

    @Column
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "situacao_cartorio", referencedColumnName = "id")
    private SituacaoCartorio situacaoCartorio;

    @ManyToMany
    @JoinTable(name = "rel_cartorio_atribuicao",
            joinColumns = @JoinColumn(name = "cartorio"),
            inverseJoinColumns = @JoinColumn(name = "atribuicao"),
            schema = ConfigUrl.SCHEMA_PROJETO)
    private List<AtribuicaoCartorio> atribuicoes;

}
