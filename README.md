# MicrosservicoA
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

## Instalação

Clone o [Repositorio](https://github.com/thejinM/Desafio3_CompassUOL.git), abra separadamente em uma janela da sua IDE a pasta 'microservicoA' e execute o Spring com a própria IDE;

Se não, utilize o seguinte comando, caso tenha o Maven instalado em seu computador.

```bash
    mvn spring-boot:run  
```

Observação: é necessário ter o MongoDB instalado e configurado corretamente.

---

## Documentação da API

Rota para acessar o Swagger: [localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

---

### Rotas de Eventos

| MÉTODO  | ROTA                               | Descrição                                     |
|:--------|:-----------------------------------| :---------------------------------------------|
| `GET`   | `api/eventos/buscaTodosEventos`               | Retorna **todos os eventos** existentes no banco  |
| `GET`   | `/api/eventos/listarEventosAlfabeticamente`| Retorna **eventos ordenados alfabeticamente** |
| `GET`   | `api/eventos/buscaEventoPorID/{id}"`| Retorna um **evento** pelo id  |
| `POST`  | `/api/eventos/criaEvento`          | **Cria** um novo evento                       |
| `PUT`   | `/api/eventos/atualizaEvento/{id}` | **Atualiza** um evento existente pelo id      |
| `DELETE`| `/api/eventos/deletaEvento/{id}`   | **Deleta** um evento pelo id                  |


___


## Considerações

- A API valida os dados antes de salvar no banco.
- A API se comunica com o ViaCEP para validar e registrar o endereço do evento a partir do CEP inserido.
- Em caso de erro, a API retorna mensagens adequadas para facilitar a depuração.

---

## Autores
- [@theJinM](https://github.com/theJinM)

