package compass.microservicoB.exception;

public class CriarIngressoException extends RuntimeException 
{
  public CriarIngressoException(Throwable cause) 
  {
    super("Requisição inválida. O eventoID é obrigatório.", cause);
  }

  public CriarIngressoException(String message, Throwable cause) 
  {
      super(message, cause);
  }
}