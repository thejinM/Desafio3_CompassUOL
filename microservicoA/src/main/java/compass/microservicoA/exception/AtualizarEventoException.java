package compass.microservicoA.exception;

public class AtualizarEventoException extends RuntimeException 
{
  public AtualizarEventoException() 
  {
    super("Requisição inválida. Dados fornecidos para atualização estão incompletos ou incorretos.");
  }
}