package compass.microservicoB.exception;

public class AtualizarIngressoException extends RuntimeException 
{
  public AtualizarIngressoException(Throwable cause) 
  {
    super("Erro ao atualizar ingresso!", cause);
  }
}