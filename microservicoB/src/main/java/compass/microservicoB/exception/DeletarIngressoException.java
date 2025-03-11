package compass.microservicoB.exception;

public class DeletarIngressoException extends RuntimeException 
{
  public DeletarIngressoException(Throwable cause) 
  {
    super("Erro ao deletar ingresso!", cause);
  }
}