package compass.microservicoB.exception;

public class CriarIngressoException extends RuntimeException 
{
  public CriarIngressoException(Throwable cause) 
  {
    super("Erro ao criar ingresso!", cause);
  }
}