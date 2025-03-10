package compass.microservicoA.dto;

import java.io.Serializable;
import java.time.Instant;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoDTO implements Serializable
{
  private String id;
  private Instant dataHora;
  private String nomeEvento;
  private String cep;
  private String logradouro;
  private String bairro;
  private String cidade;
  private String uf;
  private String descricao;
}