package compass.microservicoB.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EventoIDObrigatorioException extends ResponseStatusException 
{
  public EventoIDObrigatorioException() 
  {
    super(HttpStatus.BAD_REQUEST, "O eventoID é obrigatório.");
  }
}