package compass.microservicoB.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class HandlerIngressosException 
{
  @ExceptionHandler(AtualizarIngressoException.class)
  public ResponseEntity<Object> handleAtualizarIngressoException(AtualizarIngressoException ex) 
  {
    return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
  }

  @ExceptionHandler(CriarIngressoException.class)
  public ResponseEntity<Object> handleCriarIngressoException(CriarIngressoException ex) 
  {
    return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
  }

  @ExceptionHandler(DeletarIngressoException.class)
  public ResponseEntity<Object> handleDeletarIngressoException(DeletarIngressoException ex) 
  {
    return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
  }

  @ExceptionHandler(EventoIDObrigatorioException.class)
  public ResponseEntity<Object> handleEventoIDObrigatorioException(EventoIDObrigatorioException ex) 
  {
    return buildResponse(HttpStatus.BAD_REQUEST, ex.getReason());
  }

  @ExceptionHandler(EventoNaoEncontradoException.class)
  public ResponseEntity<Object> handleEventoNaoEncontradoException(EventoNaoEncontradoException ex) 
  {
    return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(IngressoNaoEncontradoException.class)
  public ResponseEntity<Object> handleIngressoNaoEncontradoException(IngressoNaoEncontradoException ex) 
  {
    return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) 
  {
    HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
    String mensagem = ex.getReason() != null ? ex.getReason() : "Erro inesperado.";
    return buildResponse(status, mensagem);
  }  

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleGenericException(Exception ex) 
  {
    return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro inesperado.");
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) 
  {
    return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor: " + ex.getMessage());
  } 

  private ResponseEntity<Object> buildResponse(HttpStatus status, String mensagem) 
  {
    Map<String, Object> response = new HashMap<>();
    response.put("DataHora", LocalDateTime.now());
    response.put("Status", status.value());
    response.put("Erro", status.getReasonPhrase());
    response.put("Mensagem", mensagem);

    return new ResponseEntity<>(response, status);
  }
}