package compass.microservicoB.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class HandlerIngressosException 
{
  private static final Logger logger = LoggerFactory.getLogger(HandlerIngressosException.class);

  @ExceptionHandler(AtualizarIngressoException.class)
  public ResponseEntity<Object> handleAtualizarIngressoException(AtualizarIngressoException ex) 
  {
    logger.error("Erro ao atualizar ingresso", ex);
    return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(CriarIngressoException.class)
  public ResponseEntity<Object> handleCriarIngressoException(CriarIngressoException ex) 
  {
    logger.error("Erro ao criar ingresso", ex);
    return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
  }

  @ExceptionHandler(DeletarIngressoException.class)
  public ResponseEntity<Object> handleDeletarIngressoException(DeletarIngressoException ex) 
  {
    logger.error("Erro ao deletar ingresso", ex);
    return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(EventoIDObrigatorioException.class)
  public ResponseEntity<Object> handleEventoIDObrigatorioException(EventoIDObrigatorioException ex) 
  {
    logger.error("Evento ID obrigatório não informado", ex);
    return buildResponse(HttpStatus.BAD_REQUEST, ex.getReason());
  }

  @ExceptionHandler(EventoNaoEncontradoException.class)
  public ResponseEntity<Object> handleEventoNaoEncontradoException(EventoNaoEncontradoException ex) 
  {
    logger.error("Evento não encontrado", ex);
    return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(IngressoNaoEncontradoException.class)
  public ResponseEntity<Object> handleIngressoNaoEncontradoException(IngressoNaoEncontradoException ex) 
  {
    logger.error("Ingresso não encontrado", ex);
    return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleGenericException(Exception ex) 
  {
    logger.error("Erro inesperado", ex);
    return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocorreu um erro inesperado.");
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) 
  {
    logger.error("Erro interno no servidor", ex);
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