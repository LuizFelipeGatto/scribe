CREATE TABLE IF NOT EXISTS projeto.situacao_cartorio (
    id VARCHAR(20) NOT NULL,
    nome VARCHAR(50) NOT NULL,

    CONSTRAINT situacao_cartorio_pk PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS projeto.atribuicao_cartorio (
    id VARCHAR(20) NOT NULL,
    nome VARCHAR(50) NOT NULL,
    situacao BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT atribuicao_cartorio_pk PRIMARY KEY (id)
);

CREATE SEQUENCE IF NOT EXISTS projeto.cartorio_id_seq;

CREATE TABLE IF NOT EXISTS projeto.cartorio (
    id INTEGER NOT NULL DEFAULT nextval('projeto.cartorio_id_seq'::regclass),
    nome VARCHAR(150) NOT NULL,
    observacao VARCHAR(250),
    situacao_cartorio VARCHAR(20) NOT NULL,

    CONSTRAINT cartorio_pk PRIMARY KEY (id),
    CONSTRAINT situacao_cartorio_fk FOREIGN KEY (situacao_cartorio) REFERENCES projeto.situacao_cartorio(id)
);

CREATE TABLE IF NOT EXISTS projeto.rel_cartorio_atribuicao (
    cartorio INTEGER NOT NULL,
    atribuicao VARCHAR(20) NOT NULL,
    CONSTRAINT pk_rel_cartorio_atribuicao PRIMARY KEY (cartorio, atribuicao),
    CONSTRAINT fk_rel_cartorio FOREIGN KEY (cartorio) REFERENCES projeto.cartorio(id),
    CONSTRAINT fk_rel_atribuicao FOREIGN KEY (atribuicao) REFERENCES projeto.atribuicao_cartorio(id)
);