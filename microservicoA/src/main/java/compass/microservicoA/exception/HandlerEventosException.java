package compass.microservicoA.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class HandlerEventosException 
{
  private static final Logger logger = LoggerFactory.getLogger(HandlerEventosException.class);

  @ExceptionHandler(EventoNaoEncontradoException.class)
  public ResponseEntity<Object> handleEventoNaoEncontrado(EventoNaoEncontradoException ex) 
  {
    logger.error("Erro: Evento não encontrado", ex);
    return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(CriarEventoException.class)
  public ResponseEntity<Object> handleCriarEvento(CriarEventoException ex) 
  {
    logger.error("Erro ao criar evento", ex);
    return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(AtualizarEventoException.class)
  public ResponseEntity<Object> handleAtualizarEvento(AtualizarEventoException ex) 
  {
    logger.error("Erro ao atualizar evento", ex);
    return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(DeletarEventoException.class)
  public ResponseEntity<Object> handleDeletarEvento(DeletarEventoException ex) 
  {
    logger.error("Erro ao deletar evento", ex);
    return buildResponse(HttpStatus.NOT_FOUND, ex.getReason());
  }

  @ExceptionHandler(ResponseStatusException.class)
  public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) 
  {
    HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
    String mensagem = ex.getReason() != null ? ex.getReason() : "Erro inesperado.";
    
    logger.error("ResponseStatusException capturada: {}", mensagem, ex);
    return buildResponse(status, mensagem);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleGenericException(Exception ex) 
  {
    logger.error("Erro inesperado", ex);
    return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro inesperado.");
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