package compass.microservicoB.exception;

public class CriarIngressoException extends RuntimeException 
{
  public CriarIngressoException(Throwable cause) 
  {
    super("Requisição inválida. O eventoID é obrigatório.", cause);
  }
}