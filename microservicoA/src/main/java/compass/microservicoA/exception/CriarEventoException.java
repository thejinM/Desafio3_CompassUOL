package compass.microservicoA.exception;

public class CriarEventoException extends RuntimeException 
{
  public CriarEventoException() 
  {
    super("Requisição inválida. Campos obrigatórios podem estar ausentes ou com formato incorreto.");
  }
}