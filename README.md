# Desafio3_CompassUOL
﻿ #MicroservicoA
 ## Gerenciamento de Eventos

Este projeto consiste em uma API para gerenciamento de eventos, permitindo a criação, listagem, listagem alfabética, atualização e remoção de eventos. Além disso, a API utiliza a integração com o ViaCEP para cadastrar endereços a partir do CEP informado.


---


## Tecnologias Utilizadas
- Java 17
- Spring Boot
- MongoDB
- Swagger (OpenAPI)
- Feign Client
- ViaCEP

  
---


## Documentação da API

Rota para acessar o Swagger: [localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)


---


### Rotas de Eventos

| MÉTODO  | ROTA                               | Descrição                                     |
|:--------|:-----------------------------------| :---------------------------------------------|
| `GET`   | `/api/eventos/buscaTodosEventos`               | Retorna **todos os eventos** existentes no banco  |
| `GET`   | `/api/eventos/listarEventosAlfabeticamente`| Retorna **eventos ordenados alfabeticamente** |
| `GET`   | `api/eventos/buscaEventoPorID/{id}"`| Retorna um **evento** pelo id  |
| `POST`  | `/api/eventos/criaEvento`          | **Cria** um novo evento                       |
| `PUT`   | `/api/eventos/atualizaEventoPorID/{id}` | **Atualiza** um evento existente pelo id      |
| `DELETE`| `/api/eventos/deletaEventoPorID/{id}`   | **Deleta** um evento pelo id                  |


___


## Considerações

- A API valida os dados antes de salvar no banco.
- A API se comunica com o ViaCEP para validar e registrar o endereço do evento a partir do CEP inserido.
- Em caso de erro, a API retorna mensagens adequadas para facilitar a depuração.


---


﻿# MicrosservicoB
 ## Gerenciamento de Ingressos

Este microserviço é responsável pelo gerenciamento de ingressos, permitindo a criação, listagem, atualização e remoção de ingressos. Ele consome dados do Microserviço de Eventos, garantindo a consistência das informações.


---


## Tecnologias Utilizadas
- Java 17
- Spring Boot
- MongoDB
- Swagger (OpenAPI)
- Feign Client


---


## Documentação da API

Rota para acessar o Swagger: [localhost:8081/swagger-ui/index.html#/](http://localhost:8081/swagger-ui/index.html#/)


---


### Rotas de Ingressos

| MÉTODO  | ROTA                               | Descrição                                     |
|:--------|:-----------------------------------| :---------------------------------------------|
| `GET`   | `/api/ingressos/checarIngressosPorEventoID/{eventoID}`| **Verifica** se existem **ingressos vendidos** para um evento|
| `GET`   | `/api/ingressos/buscaTodosIngressos`        | Retorna **todos os ingressos** salvos no banco  |
| `GET`   | `/api/ingressos/buscaIngressoPorID/{id}`|  Retorna um **ingresso** pelo id   |
| `GET`   | `/api/ingressos/buscarIngressosPorEventoID/{eventoID}`| **Busca** ingresso pelo **id do evento**|
| `GET`   | `/api/ingressos/buscaIngressosPorCPF/{cpf}`| **Busca** ingresso pelo **cpf** do comprador|
| `POST`  | `/api/ingressos/criaIngresso`          | **Cria** um novo ingresso                    |
| `PUT`   | `/api/ingressos/atualizaIngressoPorID/{id}` | **Atualiza** um ingresso existente pelo id      |
| `DELETE`| `/api/ingressos/deletaIngressoPorID/{id}`   | **Deleta** um ingresso pelo id                  |


---


## Considerações

- O microserviço valida os dados antes de salvar no banco.
- Ele se comunica com o Microserviço de Eventos para garantir que os ingressos sejam associados a eventos existentes.
- Em caso de erro, a API retorna mensagens adequadas para facilitar a depuração.


---





## Instalação

Clone o [Repositorio](https://github.com/thejinM/Desafio3_CompassUOL.git), abra separadamente em uma janela da sua IDE a pasta 'microservicoA' e em outra a pasta 'microservicoB', e execute o Spring de ambas aplicações com a própria IDE;


---


## Autores
- [@theJinM](https://github.com/theJinM)




