package compass.microservicoA.integracao;

import lombok.Data;

@Data
public class ViaCEPResposta
{
  private String cep;
  private String logradouro;
  private String complemento;
  private String bairro;
  private String localidade;
  private String uf;
}