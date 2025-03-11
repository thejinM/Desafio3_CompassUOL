package compass.microservicoA.exception;

public class EventoNaoEncontradoException extends RuntimeException 
{
  public EventoNaoEncontradoException() 
  {
    super("Nenhum evento encontrado!");
  }
}