package compass.microservicoB.integracao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import compass.microservicoB.entity.EventoIngresso;

@FeignClient(name = "microservicoA", url = "${microservicoA.url}")
public interface IntegracaoEvento
{
  @GetMapping("/api/eventos/buscaEventoPorID/{id}")
  EventoIngresso buscarEventoPorID(@PathVariable("id") String id);
}