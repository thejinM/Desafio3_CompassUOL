package compass.microservicoA.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DeletarEventoException extends ResponseStatusException 
{
  public DeletarEventoException() 
  {
    super(HttpStatus.CONFLICT, "O evento não pode ser deletado porque possui ingressos vendidos!");
  }
}