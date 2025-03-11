package compass.microservicoA.dto;

import java.io.Serializable;
import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;

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

  @JsonProperty(access = JsonProperty.Access.READ_ONLY) 
  private String cidade;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY) 
  private String uf;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY) 
  private String descricao;
}