package compass.microservicoB.service;

import compass.microservicoB.dto.IngressoDTO;
import compass.microservicoB.entity.EventoIngresso;
import compass.microservicoB.entity.Ingresso;
import compass.microservicoB.exception.*;
import compass.microservicoB.integracao.IntegracaoEvento;
import compass.microservicoB.repository.IngressoRepositorio;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class IngressoServicoTest 
{
  @InjectMocks
  private IngressoServico ingressoServico;

  @Mock
  private IngressoRepositorio ingressoRepositorio;

  @Mock
  private IntegracaoEvento integracaoEvento;

  private IngressoDTO ingressoDTO;
  private Ingresso ingresso;

  @BeforeEach
  void setup() 
  {
    MockitoAnnotations.openMocks(this);
    ingressoDTO = new IngressoDTO("1", "12345678901", "João Silva", "joao.silva@email.com", "1", null,new BigDecimal("100.00"), new BigDecimal("20.00"), "Comprado");
    ingresso = new Ingresso();
    ingresso.setId("1");
    ingresso.setCpf(ingressoDTO.getCpf());
    ingresso.setNomeCliente(ingressoDTO.getNomeCliente());
    ingresso.setEmailCliente(ingressoDTO.getEmailCliente());
    ingresso.setEventoID(ingressoDTO.getEventoID());
    ingresso.setValorTotalBRL(ingressoDTO.getValorTotalBRL());
    ingresso.setStatus("Confirmado!");
  }

  @Test
  void buscarIngressos_DeveRetornarListaDeIngressos() 
  {
    when(ingressoRepositorio.findAll()).thenReturn(Arrays.asList(ingresso));
    when(integracaoEvento.buscarEventoPorID(ingresso.getEventoID())).thenReturn(new EventoIngresso());

    var resultado = ingressoServico.buscarIngressos();

    assertFalse(resultado.isEmpty());
    assertEquals(1, resultado.size());
  }

  @Test
  void buscarIngressos_DeveLancarIngressoNaoEncontradoExceptionSeListaVazia() 
  {
    when(ingressoRepositorio.findAll()).thenReturn(Collections.emptyList());

    assertThrows(IngressoNaoEncontradoException.class, () -> ingressoServico.buscarIngressos());
  }

  @Test
  void buscarIngressoPorID_DeveRetornarIngresso() 
  {
    when(ingressoRepositorio.findById("1")).thenReturn(Optional.of(ingresso));
    when(integracaoEvento.buscarEventoPorID(ingresso.getEventoID())).thenReturn(new EventoIngresso());

    var resultado = ingressoServico.buscarIngressoPorID("1");

    assertNotNull(resultado);
    assertEquals(ingressoDTO.getId(), resultado.getId());
  }

  @Test
  void buscarIngressoPorID_DeveLancarIngressoNaoEncontradoExceptionSeNaoEncontrado() 
  {
    when(ingressoRepositorio.findById("1")).thenReturn(Optional.empty());

    assertThrows(IngressoNaoEncontradoException.class, () -> ingressoServico.buscarIngressoPorID("1"));
  }

  @Test
  void buscarIngressosPorEventoID_DeveRetornarListaDeIngressos() 
  {
    when(ingressoRepositorio.findByEventoID("1")).thenReturn(Arrays.asList(ingresso));
    when(integracaoEvento.buscarEventoPorID("1")).thenReturn(new EventoIngresso());

    var resultado = ingressoServico.buscarIngressosPorEventoID("1");

    assertFalse(resultado.isEmpty());
  }

  @Test
  void buscarIngressosPorEventoID_DeveLancarIngressoNaoEncontradoExceptionSeNaoEncontrado() 
  {
    when(ingressoRepositorio.findByEventoID("1")).thenReturn(Collections.emptyList());

    assertThrows(IngressoNaoEncontradoException.class, () -> ingressoServico.buscarIngressosPorEventoID("1"));
  }

  @Test
  void buscarIngressosPorEventoID_DeveChamarRepositorioCorretamente() 
  {
    when(ingressoRepositorio.findByEventoID("1")).thenReturn(Arrays.asList(ingresso));
    when(integracaoEvento.buscarEventoPorID("1")).thenReturn(new EventoIngresso());

    ingressoServico.buscarIngressosPorEventoID("1");

    verify(ingressoRepositorio, times(1)).findByEventoID("1");
    verify(integracaoEvento, times(1)).buscarEventoPorID("1");
  }

  @Test
  void criarIngresso_DeveCriarIngresso() 
  {
    EventoIngresso evento = new EventoIngresso();
    when(integracaoEvento.buscarEventoPorID(ingressoDTO.getEventoID())).thenReturn(evento);
    when(ingressoRepositorio.save(any(Ingresso.class))).thenReturn(ingresso);

    IngressoDTO resultado = ingressoServico.criarIngresso(ingressoDTO);

    assertNotNull(resultado);
    assertEquals(ingressoDTO.getCpf(), resultado.getCpf());
  }

  @Test
  void criarIngresso_DeveLancarEventoIDObrigatorioExceptionSeEventoIDNaoForInformado() 
  {
    ingressoDTO.setEventoID(null);

    assertThrows(EventoIDObrigatorioException.class, () -> ingressoServico.criarIngresso(ingressoDTO));
  }

  @Test
  void criarIngresso_DeveLancarEventoNaoEncontradoExceptionSeEventoNaoExistir() 
  {
    when(integracaoEvento.buscarEventoPorID(ingressoDTO.getEventoID())).thenReturn(null);

    assertThrows(EventoNaoEncontradoException.class, () -> ingressoServico.criarIngresso(ingressoDTO));
  }

  @Test
  void criarIngresso_DeveLancarCriarIngressoExceptionEmCasoDeErro() 
  {
    when(integracaoEvento.buscarEventoPorID(ingressoDTO.getEventoID())).thenThrow(FeignException.NotFound.class);

    assertThrows(CriarIngressoException.class, () -> ingressoServico.criarIngresso(ingressoDTO));
  }

  @Test
  void criarIngresso_DeveLancarCriarIngressoExceptionSeSaveFalhar() 
  {
    EventoIngresso evento = new EventoIngresso();
    when(integracaoEvento.buscarEventoPorID(ingressoDTO.getEventoID())).thenReturn(evento);
    when(ingressoRepositorio.save(any(Ingresso.class))).thenThrow(new RuntimeException());

    assertThrows(CriarIngressoException.class, () -> ingressoServico.criarIngresso(ingressoDTO));
  }

  @Test
  void atualizarIngressoPorID_DeveAtualizarIngresso() 
  {
    when(ingressoRepositorio.findById("1")).thenReturn(Optional.of(ingresso));
    when(ingressoRepositorio.save(any(Ingresso.class))).thenReturn(ingresso);

    ingressoDTO.setStatus("Confirmado!");
    IngressoDTO resultado = ingressoServico.atualizarIngressoPorID("1", ingressoDTO);

    assertNotNull(resultado);
    assertEquals("Confirmado!", resultado.getStatus());
  }

  @Test
  void atualizarIngressoPorID_DeveLancarIngressoNaoEncontradoExceptionSeNaoEncontrado() 
  {
    when(ingressoRepositorio.findById("1")).thenReturn(Optional.empty());

    assertThrows(IngressoNaoEncontradoException.class, () -> ingressoServico.atualizarIngressoPorID("1", ingressoDTO));
  }

  @Test
  void atualizarIngressoPorID_DeveLancarAtualizarIngressoExceptionEmCasoDeErro() 
  {
    when(ingressoRepositorio.findById("1")).thenReturn(Optional.of(ingresso));
    when(ingressoRepositorio.save(any(Ingresso.class))).thenThrow(new RuntimeException());

    assertThrows(AtualizarIngressoException.class, () -> ingressoServico.atualizarIngressoPorID("1", ingressoDTO));
  }

  @Test
  void deletarIngressoPorID_DeveDeletarIngresso() 
  {
    when(ingressoRepositorio.findById("1")).thenReturn(Optional.of(ingresso));
    doNothing().when(ingressoRepositorio).delete(ingresso);

    ingressoServico.deletarIngressoPorID("1");

    verify(ingressoRepositorio).delete(ingresso);
  }

  @Test
  void deletarIngressoPorID_DeveLancarIngressoNaoEncontradoExceptionSeNaoEncontrado() 
   {
    when(ingressoRepositorio.findById("1")).thenReturn(Optional.empty());

    assertThrows(IngressoNaoEncontradoException.class, () -> ingressoServico.deletarIngressoPorID("1"));
  }

  @Test
  void deletarIngressoPorID_DeveLancarDeletarIngressoExceptionEmCasoDeErro()
  {
    when(ingressoRepositorio.findById("1")).thenReturn(Optional.of(ingresso));
    doThrow(new RuntimeException()).when(ingressoRepositorio).delete(ingresso);

    assertThrows(DeletarIngressoException.class, () -> ingressoServico.deletarIngressoPorID("1"));
  }
}