package compass.microservicoB.exception;

public class IngressoNaoEncontradoException extends RuntimeException 
{
  public IngressoNaoEncontradoException() 
  {
    super("Ingresso não encontrado.");
  }
}