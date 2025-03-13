package compass.microservicoA.exception;

public class EventoNaoEncontradoException extends RuntimeException 
{
  public EventoNaoEncontradoException() 
  {
    super("Evento não encontrado.");
  }
}