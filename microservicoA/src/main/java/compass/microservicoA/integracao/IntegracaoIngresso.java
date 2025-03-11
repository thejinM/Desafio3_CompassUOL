package compass.microservicoA.integracao;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservicoB", url = "${microservicoB.url}")
public interface IntegracaoIngresso 
{
  @GetMapping("/checarIngressosPorEventoID/{eventoID}")
  Map<String, Object> verificarIngressos(@PathVariable String eventoID);
}