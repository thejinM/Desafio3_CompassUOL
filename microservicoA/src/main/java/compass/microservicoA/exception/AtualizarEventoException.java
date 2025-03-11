package compass.microservicoA.exception;

public class AtualizarEventoException extends RuntimeException 
{
  public AtualizarEventoException() 
  {
    super("Erro ao atualizar evento!");
  }
}