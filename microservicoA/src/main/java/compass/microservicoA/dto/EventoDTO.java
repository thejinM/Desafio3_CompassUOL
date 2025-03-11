package compass.microservicoA.dto;

import java.io.Serializable;
import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoDTO implements Serializable
{
  @Schema(hidden = true)
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