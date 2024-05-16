# SCRIBE

Projeto para implementar um micro-serviço para uma API de cartórios.
Desenvolvido nas seguintes tecnologias:

  - Java
  - SpringBoot
  - Flyway
  - PostgreSQL

# Informações importantes

Abra o projeto em alguma IDE com suporte para Java, abra o aquivo pom.xml e coloque para baixar as dependências que contém nele.
Após isso, crie um banco de dados postgreSQL e abra o arquivo ``application.properties`` e configure para o ``nome do banco de dados`` que criou, seu ``nome de usuário`` e ``senha`` do banco de dados. 

     spring.datasource.url = jdbc:postgresql://localhost:5432/scribe
     spring.datasource.username = postgres
     spring.datasource.password = postgres

O flyway criará automaticamente o schema no banco de dados, as tabelas e a migration de popular as tabelas. Feito isso, o sistema estará configurado para aceitar as requisições via ```POSTMAN``` ou ```THUNDER CLIENT do VsCode```.

# API'S

As operações seguirão o seguinte padrão: http://locahost:9564/nomeTabela

- Atribuição cartório
    - Listar todos, método GET: http://locahost:9564/atribuicao
    - Listar todos com paginação, método POST http://locahost:9564/atribuicao/filter
      - BODY: ```
                {},
                {"nome": "nomeProcurado"}, 
                {"id": "idProcurado"} ou 
                {"nome": "nomeProcurado", "id": "idProcurado"}
              ```
    - Procurar por id, método GET: http://locahost:9564/atribuicao/{id}
    - Inserir registro, método POST: http://locahost:9564/atribuicao
      - BODY ```
              {"id": "idInserir", "nome": "nomeInserir"}`` ou 
              {"id": "idInserir", "nome": "nomeInserir", "situacao": situacao}
              ```
    - Alterar registro, método PUT: http://locahost:9564/atribuicao/{id}
      - BODY ```
              {"nome": "nomeAlterar"}`` ou 
              {"nome": "nomeAlterar", "situacao": situacao}
              ```
    - Deletar registro, método DELETE: http://locahost:9564/atribuicao/{id}

- Situação cartório
    - Listar todos, método GET: http://locahost:9564/situacao
    - Listar todos com paginação, método POST http://locahost:9564/situacao/filter
      - BODY: ```
                {},
                {"nome": "nomeProcurado"}, 
                {"id": "idProcurado"} ou 
                {"nome": "nomeProcurado", "id": "idProcurado"}
              ```
    - Procurar por id, método GET: http://locahost:9564/situacao/{id}
    - Inserir registro, método POST: http://locahost:9564/situacao
      - BODY ```{"id": "idInserir", "nome": "nomeInserir"}```
    - Alterar registro, método PUT: http://locahost:9564/situacao/{id}
      - BODY ```{"nome": "nomeAlterar"}```
    - Deletar registro, método DELETE: http://locahost:9564/situacao/{id}
 
- Cartório
    - Listar todos, método GET: http://locahost:9564/cartorio
    - Listar todos com paginação, método POST http://locahost:9564/cartorio/filter
      - BODY: ```
                {},
                {"nome": "nomeProcurado"}, 
                {"id": "idProcurado"} ou 
                {"nome": "nomeProcurado", "id": "idProcurado"}
              ```
    - Procurar por id, método GET: http://locahost:9564/cartorio/{id}
    - Inserir registro, método POST: http://locahost:9564/cartorio
        - BODY
            JSON:
             ```{
                 {
                    "id": "idInserir",
                    "nome": "nomeAlterar",
                    "observacao": "observacaoAlterar",
                    "situacao": {
                        "id": "idSituacaoCadastrada"
                    },
                    "atribuicoes": [
                        {
                          "id": "idAtribuicaoSalvo"
                        },
                        {
                          "id": "idAtribuicaoSalvo"
                        }
                    ]
                  }
      
    - Alterar registro, método PUT: http://locahost:9564/cartorio/{id}
        - BODY
            JSON:
             ```{
                 {
                    "nome": "nomeAlterar",
                    "observacao": "observacaoAlterar",
                    "situacao": {
                        "id": "idSituacaoCadastrada"
                    },
                    "atribuicoes": [
                        {
                          "id": "idAtribuicaoSalvo"
                        },
                        {
                          "id": "idAtribuicaoSalvo"
                        }
                    ]
                  }
          
    - Deletar registro, método DELETE: http://locahost:9564/cartorio/{id}

