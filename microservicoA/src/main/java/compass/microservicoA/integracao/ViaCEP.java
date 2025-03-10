package compass.microservicoA.integracao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "viacep", url = "https://viacep.com.br/ws/")
public interface ViaCEP
{
  @GetMapping("{cep}/json/")
  ViaCEPResposta consultarCEP(@PathVariable("cep") String cep);
}
