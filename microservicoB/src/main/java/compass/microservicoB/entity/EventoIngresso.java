package compass.microservicoB.entity;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoIngresso implements Serializable
{
  private String id;
  private Instant dataHora;
  private String nomeEvento;
  private String logradouro;
  private String bairro;
  private String cidade;
  private String uf;
  private String descricao;
}