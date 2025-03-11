package compass.microservicoA.exception;

public class CriarEventoException extends RuntimeException 
{
  public CriarEventoException() 
  {
    super("Erro ao criar evento!");
  }
}