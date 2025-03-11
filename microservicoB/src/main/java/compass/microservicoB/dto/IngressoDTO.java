package compass.microservicoB.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import compass.microservicoB.entity.EventoIngresso;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IngressoDTO implements Serializable
{
  @Schema(hidden = true)
  private String id;

  private String cpf;
  private String nomeCliente;
  private String emailCliente;
 
  @NotNull(message = "O eventoID é obrigatório!")
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String eventoID;

  @Schema(hidden = true) 
  private EventoIngresso evento;

  private BigDecimal valorTotalBRL;
  
  @Schema(hidden = true) 
  private BigDecimal valorTotalUSD;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String status;
}