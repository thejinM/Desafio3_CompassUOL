package compass.microservicoA.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import compass.microservicoA.dto.EventoDTO;
import compass.microservicoA.entity.Evento;
import compass.microservicoA.exception.*;
import compass.microservicoA.integracao.IntegracaoIngresso;
import compass.microservicoA.integracao.ViaCEP;
import compass.microservicoA.integracao.ViaCEPResposta;
import compass.microservicoA.repository.EventoRepositorio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.Instant;
import java.util.*;

class EventoServicoTest 
{
  @InjectMocks
  private EventoServico eventoServico;

  @Mock
  private EventoRepositorio eventoRepositorio;

  @Mock
  private ViaCEP viaCEP;

  @Mock
  private IntegracaoIngresso integracaoIngresso;

  private EventoDTO eventoDTO;
  private Evento evento;

  @BeforeEach
  void setup() 
  {
    MockitoAnnotations.openMocks(this);
    eventoDTO = new EventoDTO("1", Instant.now(), "Evento Teste", "01001000", "Rua Teste", "Centro", "São Paulo", "SP","Descrição Teste");
    evento = new Evento("1", eventoDTO.getDataHora(), eventoDTO.getNomeEvento(), eventoDTO.getCep(), eventoDTO.getLogradouro(), eventoDTO.getBairro(), eventoDTO.getCidade(), eventoDTO.getUf(), eventoDTO.getDescricao());
  }

  @Test
  void buscarEventos_DeveRetornarListaDeEventos() 
  {
    when(eventoRepositorio.findAll()).thenReturn(Arrays.asList(evento));
    var resultado = eventoServico.buscarEventos();
    assertFalse(resultado.isEmpty());
    assertEquals(1, resultado.size());
  }

  @Test
  void buscarEventos_DeveLancarEventoNaoEncontradoExceptionSeListaVazia() 
  {
    when(eventoRepositorio.findAll()).thenReturn(Collections.emptyList());
    assertThrows(EventoNaoEncontradoException.class, () -> eventoServico.buscarEventos());
  }

  @Test
  void buscarEventoPorID_DeveRetornarEvento() 
  {
    when(eventoRepositorio.findById("1")).thenReturn(Optional.of(evento));
    var resultado = eventoServico.buscarEventoPorID("1");
    assertNotNull(resultado);
    assertEquals(eventoDTO.getId(), resultado.getId());
  }

  @Test
  void buscarEventoPorID_DeveLancarEventoNaoEncontradoExceptionSeNaoEncontrado() 
  {
    when(eventoRepositorio.findById("1")).thenReturn(Optional.empty());
    assertThrows(EventoNaoEncontradoException.class, () -> eventoServico.buscarEventoPorID("1"));
  }

  @Test
  void buscarEventosOrdenados_DeveRetornarListaOrdenada() 
  {
    when(eventoRepositorio.findAllByOrderByNomeEventoAsc()).thenReturn(Arrays.asList(evento));
    var resultado = eventoServico.buscarEventosOrdenados();
    assertFalse(resultado.isEmpty());
  }

  @Test
  void buscarEventosOrdenados_DeveLancarEventoNaoEncontradoExceptionSeListaVazia() 
  {
    when(eventoRepositorio.findAllByOrderByNomeEventoAsc()).thenReturn(Collections.emptyList());
    assertThrows(EventoNaoEncontradoException.class, () -> eventoServico.buscarEventosOrdenados());
  }

  @Test
  void criarEvento_DeveCriarEvento() 
  {
    ViaCEPResposta viaCEPResposta = new ViaCEPResposta("01001000", "Rua Teste", "Centro", "São Paulo", "SP");
    when(viaCEP.consultarCEP(eventoDTO.getCep())).thenReturn(viaCEPResposta);
    when(eventoRepositorio.save(any(Evento.class))).thenReturn(evento);
    EventoDTO resultado = eventoServico.criarEvento(eventoDTO);
    assertNotNull(resultado);
    assertEquals(eventoDTO.getNomeEvento(), resultado.getNomeEvento());
  }

  @Test
  void criarEvento_DeveLancarCriarEventoExceptionEmCasoDeErro() 
  {
    when(viaCEP.consultarCEP(eventoDTO.getCep())).thenThrow(new RuntimeException());
    assertThrows(CriarEventoException.class, () -> eventoServico.criarEvento(eventoDTO));
  }

  @Test
  void criarEvento_DeveLancarCriarEventoExceptionSeViaCepRetornarNulo() 
  {
    when(viaCEP.consultarCEP(eventoDTO.getCep())).thenReturn(null);
    assertThrows(CriarEventoException.class, () -> eventoServico.criarEvento(eventoDTO));
  }

  @Test
  void atualizarEventoPorID_DeveAtualizarEvento() 
  {
    when(eventoRepositorio.findById("1")).thenReturn(Optional.of(evento));
    when(eventoRepositorio.save(any(Evento.class))).thenReturn(evento);
    ViaCEPResposta viaCEPResposta = new ViaCEPResposta("01001000", "Rua Teste Atualizada", "Bairro Atualizado","São Paulo", "SP");
    when(viaCEP.consultarCEP(eventoDTO.getCep())).thenReturn(viaCEPResposta);
    EventoDTO resultado = eventoServico.atualizarEventoPorID("1", eventoDTO);
    assertNotNull(resultado);
    assertEquals("Rua Teste", resultado.getLogradouro()); 
    assertEquals("Centro", resultado.getBairro());
    assertEquals("Evento Teste", resultado.getNomeEvento());
    assertEquals(eventoDTO.getDescricao(), resultado.getDescricao()); 
  }

  @Test
  void atualizarEventoPorID_DeveLancarAtualizarEventoExceptionSeNaoEncontrado() 
  {
    when(eventoRepositorio.findById("1")).thenReturn(Optional.empty());
    assertThrows(AtualizarEventoException.class, () -> eventoServico.atualizarEventoPorID("1", eventoDTO));
  }

  @Test
  void atualizarEventoPorID_DeveAtualizarEventoSemAlterarCEP() 
  {
    when(eventoRepositorio.findById("1")).thenReturn(Optional.of(evento));
    when(eventoRepositorio.save(any(Evento.class))).thenReturn(evento);

    EventoDTO resultado = eventoServico.atualizarEventoPorID("1", eventoDTO);
    assertNotNull(resultado);
    assertEquals(evento.getCep(), resultado.getCep());
    verify(viaCEP, never()).consultarCEP(anyString()); // Garantir que o CEP não foi consultado
  }

  @Test
  void deletarEventoPorID_DeveDeletarEvento() 
  {
    when(eventoRepositorio.findById("1")).thenReturn(Optional.of(evento));
    when(integracaoIngresso.verificarIngressos("1")).thenReturn(Collections.singletonMap("existemIngressos", false));

    eventoServico.deletarEventoPorID("1");
    verify(eventoRepositorio).delete(evento);
  }

  @Test
  void deletarEventoPorID_DeveLancarDeletarEventoExceptionSeExistiremIngressos() 
  {
    when(eventoRepositorio.findById("1")).thenReturn(Optional.of(evento));
    when(integracaoIngresso.verificarIngressos("1")).thenReturn(Collections.singletonMap("existemIngressos", true));

    assertThrows(DeletarEventoException.class, () -> eventoServico.deletarEventoPorID("1"));
  }

  @Test
  void deletarEventoPorID_DeveLancarEventoNaoEncontradoExceptionSeIdInexistente() 
  {
    when(eventoRepositorio.findById("1")).thenReturn(Optional.empty());
    assertThrows(EventoNaoEncontradoException.class, () -> eventoServico.deletarEventoPorID("1"));
  }

  @Test
  void deletarEventoPorID_DeveLancarDeletarEventoExceptionSeErroDeDelecao() 
  {
    when(eventoRepositorio.findById("1")).thenReturn(Optional.of(evento));
    when(integracaoIngresso.verificarIngressos("1")).thenReturn(Collections.singletonMap("existemIngressos", false));
    doThrow(new RuntimeException()).when(eventoRepositorio).delete(evento);

    assertThrows(DeletarEventoException.class, () -> eventoServico.deletarEventoPorID("1"));
  }
}