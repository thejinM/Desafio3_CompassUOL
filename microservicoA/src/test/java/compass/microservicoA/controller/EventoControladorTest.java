package compass.microservicoA.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import compass.microservicoA.dto.EventoDTO;
import compass.microservicoA.exception.EventoNaoEncontradoException;
import compass.microservicoA.service.EventoServico;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class EventoControladorTest 
{
  @InjectMocks
  private EventoControlador eventoControlador;

  @Mock
  private EventoServico eventoServico;

  private EventoDTO eventoDTO;

  @BeforeEach
  void setup() 
  {
    MockitoAnnotations.openMocks(this);
    eventoDTO = new EventoDTO("1", Instant.now(), "Evento Teste", "01001000", "Rua Teste", "Centro", "São Paulo", "SP","Descrição Teste");
  }

  @Test
  void buscaTodosEventos_DeveRetornarListaDeEventos() 
  {
    when(eventoServico.buscarEventos()).thenReturn(Collections.singletonList(eventoDTO));
    ResponseEntity<List<EventoDTO>> resposta = eventoControlador.buscaTodosEventos();
    assertEquals(200, resposta.getStatusCode().value());
  }

  @Test
  void buscaTodosEventos_DeveRetornarNoContentSeListaVazia() 
  {
    when(eventoServico.buscarEventos()).thenReturn(Collections.emptyList());
    ResponseEntity<List<EventoDTO>> resposta = eventoControlador.buscaTodosEventos();
    assertEquals(204, resposta.getStatusCode().value());
  }

  @Test
  void buscaEventoPorID_DeveRetornarEvento() 
  {
    when(eventoServico.buscarEventoPorID("1")).thenReturn(eventoDTO);
    ResponseEntity<EventoDTO> resposta = eventoControlador.buscaEventoPorID("1");
    assertEquals(200, resposta.getStatusCode().value());
    assertNotNull(resposta.getBody());
  }

  @Test
  void buscaEventoPorID_DeveRetornarNotFoundSeNaoEncontrado() 
  {
    when(eventoServico.buscarEventoPorID("1")).thenThrow(new EventoNaoEncontradoException());
    ResponseEntity<EventoDTO> resposta = eventoControlador.buscaEventoPorID("1");
    assertEquals(404, resposta.getStatusCode().value());
  }

  @Test
  void eventosOrdenados_DeveRetornarListaOrdenada() 
  {
    when(eventoServico.buscarEventosOrdenados()).thenReturn(Arrays.asList(eventoDTO));
    ResponseEntity<List<EventoDTO>> resposta = eventoControlador.eventosOrdenados();
    assertEquals(200, resposta.getStatusCode().value());
  }

  @Test
  void eventosOrdenados_DeveRetornarNoContentSeVazio() 
  {
    when(eventoServico.buscarEventosOrdenados()).thenReturn(Collections.emptyList());
    ResponseEntity<List<EventoDTO>> resposta = eventoControlador.eventosOrdenados();
    assertEquals(204, resposta.getStatusCode().value());
  }

  @Test
  void criarEvento_DeveRetornarEventoCriado() 
  {
    when(eventoServico.criarEvento(any(EventoDTO.class))).thenReturn(eventoDTO);
    ResponseEntity<EventoDTO> resposta = eventoControlador.criarEvento(eventoDTO);
    assertEquals(201, resposta.getStatusCode().value());
    assertNotNull(resposta.getBody());
  }

  @Test
  void atualizarEventoPorID_DeveRetornarEventoAtualizado() 
  {
    when(eventoServico.atualizarEventoPorID(eq("1"), any(EventoDTO.class))).thenReturn(eventoDTO);
    ResponseEntity<EventoDTO> resposta = eventoControlador.atualizarEventoPorID("1", eventoDTO);
    assertEquals(200, resposta.getStatusCode().value());
  }

  @Test
  void deletarEventoPorID_DeveRetornarNoContent() 
  {
    doNothing().when(eventoServico).deletarEventoPorID("1");
    ResponseEntity<Void> resposta = eventoControlador.deletarEventoPorID("1");
    assertEquals(204, resposta.getStatusCode().value());
  }
}