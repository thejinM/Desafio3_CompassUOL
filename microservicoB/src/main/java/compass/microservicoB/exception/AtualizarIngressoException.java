package compass.microservicoB.exception;

public class AtualizarIngressoException extends RuntimeException 
{
  public AtualizarIngressoException(Throwable cause) 
  {
    super("Requisição inválida. Dados fornecidos para atualização do ingresso estão incompletos ou incorretos.", cause);
  }
}