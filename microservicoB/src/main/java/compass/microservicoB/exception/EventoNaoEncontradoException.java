package compass.microservicoB.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EventoNaoEncontradoException extends ResponseStatusException 
{
  public EventoNaoEncontradoException() 
  {
    super(HttpStatus.NOT_FOUND, "Evento não encontrado.");
  }
}