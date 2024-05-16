-- populando tabela situação cartório
INSERT INTO projeto.situacao_cartorio VALUES('SIT_ATIVO', 'Ativo');
INSERT INTO projeto.situacao_cartorio VALUES('SIT_BLOQUEADO', 'Bloqueado');

-- populando tabela atribuição cartório
INSERT INTO projeto.atribuicao_cartorio VALUES('CIVEL', 'Cível');
INSERT INTO projeto.atribuicao_cartorio VALUES('TRABALHISTA', 'Trabalhista', false);