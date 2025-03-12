package compass.microservicoB.controller;

import compass.microservicoB.dto.IngressoDTO;
import compass.microservicoB.service.IngressoServico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class IngressoControladorTest 
{
  @InjectMocks
  private IngressoControlador ingressoControlador;

  @Mock
  private IngressoServico ingressoServico;

  private IngressoDTO ingressoDTO;

  @BeforeEach
  void setup() 
  {
    MockitoAnnotations.openMocks(this);
    ingressoDTO = new IngressoDTO("1", "12345678901", "João Silva", "joao.silva@email.com", "1", null, new BigDecimal("100.00"), new BigDecimal("20.00"), "Comprado");
  }

  @Test
  void buscaTodosIngressos_DeveRetornarListaDeIngressos() 
  {
    when(ingressoServico.buscarIngressos()).thenReturn(Arrays.asList(ingressoDTO));
    ResponseEntity<List<IngressoDTO>> resultado = ingressoControlador.buscaTodosIngressos();

    assertEquals(HttpStatus.OK, resultado.getStatusCode());
  }

  @Test
  void buscaTodosIngressos_DeveRetornarNoContentSeNenhumIngressoEncontrado() 
  {
    when(ingressoServico.buscarIngressos()).thenReturn(Collections.emptyList());
    ResponseEntity<List<IngressoDTO>> resultado = ingressoControlador.buscaTodosIngressos();

    assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
  }

  @Test
  void buscaIngressoPorID_DeveRetornarIngresso() 
  {
    when(ingressoServico.buscarIngressoPorID("1")).thenReturn(ingressoDTO);
    ResponseEntity<IngressoDTO> resultado = ingressoControlador.buscaIngressoPorID("1");

    assertEquals(HttpStatus.OK, resultado.getStatusCode());
    assertNotNull(resultado.getBody());
  }

  @Test
  void buscaIngressoPorID_DeveRetornarNotFoundSeIngressoNaoEncontrado() 
  {
    when(ingressoServico.buscarIngressoPorID("1")).thenReturn(null);
    ResponseEntity<IngressoDTO> resultado = ingressoControlador.buscaIngressoPorID("1");

    assertEquals(HttpStatus.NOT_FOUND, resultado.getStatusCode());
  }

  @Test
  void buscarIngressosPorEventoID_DeveRetornarIngressos() 
  {
    when(ingressoServico.buscarIngressosPorEventoID("1")).thenReturn(Arrays.asList(ingressoDTO));
    ResponseEntity<List<IngressoDTO>> resultado = ingressoControlador.buscarIngressosPorEventoID("1");

    assertEquals(HttpStatus.OK, resultado.getStatusCode());
  }

  @Test
  void buscarIngressosPorEventoID_DeveRetornarNoContentSeNenhumIngressoEncontrado() 
  {
    when(ingressoServico.buscarIngressosPorEventoID("1")).thenReturn(Collections.emptyList());
    ResponseEntity<List<IngressoDTO>> resultado = ingressoControlador.buscarIngressosPorEventoID("1");

    assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
  }

  @Test
  void buscarIngressosPorCPF_DeveRetornarIngressos() 
  {
    when(ingressoServico.buscarIngressosPorCPF("12345678901")).thenReturn(Arrays.asList(ingressoDTO));
    ResponseEntity<List<IngressoDTO>> resultado = ingressoControlador.buscarIngressosPorCPF("12345678901");

    assertEquals(HttpStatus.OK, resultado.getStatusCode());
  }

  @Test
  void buscarIngressosPorCPF_DeveRetornarNoContentSeNenhumIngressoEncontrado() 
  {
    when(ingressoServico.buscarIngressosPorCPF("12345678901")).thenReturn(Collections.emptyList());
    ResponseEntity<List<IngressoDTO>> resultado = ingressoControlador.buscarIngressosPorCPF("12345678901");

    assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
  }

  @Test
  void criarIngresso_DeveCriarIngresso() 
  {
    when(ingressoServico.criarIngresso(any(IngressoDTO.class))).thenReturn(ingressoDTO);
    ResponseEntity<IngressoDTO> resultado = ingressoControlador.criarIngresso(ingressoDTO);

    assertEquals(HttpStatus.CREATED, resultado.getStatusCode());
    assertNotNull(resultado.getBody());
  }

  @Test
  void atualizarIngressoPorID_DeveAtualizarIngresso() 
  {
    when(ingressoServico.atualizarIngressoPorID("1", ingressoDTO)).thenReturn(ingressoDTO);
    ResponseEntity<IngressoDTO> resultado = ingressoControlador.atualizarIngressoPorID("1", ingressoDTO);

    assertEquals(HttpStatus.OK, resultado.getStatusCode());
    assertNotNull(resultado.getBody());
  }

  @Test
  void atualizarIngressoPorID_DeveRetornarNotFoundSeIngressoNaoEncontrado() 
  {
    when(ingressoServico.atualizarIngressoPorID("1", ingressoDTO)).thenReturn(null);
    ResponseEntity<IngressoDTO> resultado = ingressoControlador.atualizarIngressoPorID("1", ingressoDTO);

    assertEquals(HttpStatus.NOT_FOUND, resultado.getStatusCode());
  }

  @Test
  void deletarIngressoPorID_DeveDeletarIngresso() 
  {
    when(ingressoServico.buscarIngressoPorID("1")).thenReturn(ingressoDTO);
    doNothing().when(ingressoServico).deletarIngressoPorID("1");

    ResponseEntity<Void> resultado = ingressoControlador.deletarIngressoPorID("1");

    assertEquals(HttpStatus.NO_CONTENT, resultado.getStatusCode());
    verify(ingressoServico).deletarIngressoPorID("1");
  }

  @Test
  void deletarIngressoPorID_DeveRetornarNotFoundSeIngressoNaoEncontrado() 
  {
    when(ingressoServico.buscarIngressoPorID("1")).thenReturn(null);
    ResponseEntity<Void> resultado = ingressoControlador.deletarIngressoPorID("1");

    assertEquals(HttpStatus.NOT_FOUND, resultado.getStatusCode());
  }

  @Test
  void checarIngressosPorEventoID_DeveRetornarExistemIngressosTrue() 
  {
    when(ingressoServico.buscarIngressosPorEventoID("1")).thenReturn(Arrays.asList(ingressoDTO));
    ResponseEntity<Map<String, Object>> resultado = ingressoControlador.checarIngressosPorEventoID("1");

    assertEquals(HttpStatus.OK, resultado.getStatusCode());
  }

  @Test
  void checarIngressosPorEventoID_DeveRetornarExistemIngressosFalse() 
  {
    when(ingressoServico.buscarIngressosPorEventoID("1")).thenReturn(Collections.emptyList());
    ResponseEntity<Map<String, Object>> resultado = ingressoControlador.checarIngressosPorEventoID("1");

    assertEquals(HttpStatus.NOT_FOUND, resultado.getStatusCode());
  }
}