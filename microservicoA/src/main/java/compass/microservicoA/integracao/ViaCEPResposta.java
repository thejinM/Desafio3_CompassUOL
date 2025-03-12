package compass.microservicoA.integracao;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ViaCEPResposta
{
  private String cep;
  private String logradouro;
  private String bairro;
  private String localidade;
  private String uf;
}