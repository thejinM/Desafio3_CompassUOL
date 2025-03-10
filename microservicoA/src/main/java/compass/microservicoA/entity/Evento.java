package compass.microservicoA.entity;

import lombok.*;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "eventos")
public class Evento implements Serializable
{
  @Id
  private String id;

  @Field("dataHora")
  private Instant dataHora;

  private String nomeEvento;
  private String cep;
  private String logradouro;
  private String bairro;
  private String cidade;
  private String uf;
  private String descricao;
}