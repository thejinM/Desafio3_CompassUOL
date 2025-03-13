package compass.microservicoA.service;

import compass.microservicoA.dto.EventoDTO;
import compass.microservicoA.entity.Evento;
import compass.microservicoA.exception.AtualizarEventoException;
import compass.microservicoA.exception.CriarEventoException;
import compass.microservicoA.exception.DeletarEventoException;
import compass.microservicoA.exception.EventoNaoEncontradoException;
import compass.microservicoA.integracao.IntegracaoIngresso;
import compass.microservicoA.integracao.ViaCEP;
import compass.microservicoA.integracao.ViaCEPResposta;
import compass.microservicoA.repository.EventoRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EventoServico
{
  @Autowired
  private EventoRepositorio eventoRepositorio;

  @Autowired
  private ViaCEP viaCEP;

  @Autowired
  private IntegracaoIngresso integracaoIngresso;

  public List<EventoDTO> buscarEventos()
  {
    List<Evento> eventos = eventoRepositorio.findAll();

    if (eventos.isEmpty())
    {
      throw new EventoNaoEncontradoException();
    }

    return eventos.stream().map(this::paraEventoDTO).collect(Collectors.toList());
  }

  public EventoDTO buscarEventoPorID(String id)
  {
    return eventoRepositorio.findById(id).map(this::paraEventoDTO).orElseThrow(() -> new EventoNaoEncontradoException());
  }

  public List<EventoDTO> buscarEventosOrdenados() 
  {
    List<Evento> eventos = eventoRepositorio.findAllByOrderByNomeEventoAsc();

    if (eventos.isEmpty()) 
    {
      throw new EventoNaoEncontradoException();
    }
  
    return eventos.stream().map(this::paraEventoDTO).collect(Collectors.toList());
  }

  public EventoDTO criarEvento(EventoDTO eventoDTO)
  {
    try
    {
      ViaCEPResposta viaCep = viaCEP.consultarCEP(eventoDTO.getCep());
      Evento evento = new Evento();

      evento.setDataHora(eventoDTO.getDataHora());
      evento.setNomeEvento(eventoDTO.getNomeEvento());
      evento.setCep(eventoDTO.getCep());
      evento.setLogradouro(viaCep.getLogradouro());
      evento.setBairro(viaCep.getBairro());
      evento.setCidade(viaCep.getLocalidade());
      evento.setUf(viaCep.getUf());
      evento.setDescricao(eventoDTO.getDescricao());

      return paraEventoDTO(eventoRepositorio.save(evento));
    }
    catch (Exception e)
    {
      throw new CriarEventoException();
    } 
  }

  public EventoDTO atualizarEventoPorID(String id, EventoDTO eventoDTO)
  {
    try 
    {
      Evento eventoAtualizado = eventoRepositorio.findById(id).orElseThrow(() -> new EventoNaoEncontradoException());

      eventoAtualizado.setNomeEvento(eventoDTO.getNomeEvento());
      eventoAtualizado.setDataHora(eventoDTO.getDataHora());
      eventoAtualizado.setDescricao(eventoDTO.getDescricao());
      
      if (!eventoAtualizado.getCep().equals(eventoDTO.getCep())) 
      {
        ViaCEPResposta viaCepRetorno = viaCEP.consultarCEP(eventoDTO.getCep());
        eventoAtualizado.setCep(viaCepRetorno.getCep());
        eventoAtualizado.setLogradouro(viaCepRetorno.getLogradouro());
        eventoAtualizado.setBairro(viaCepRetorno.getBairro());
        eventoAtualizado.setCidade(viaCepRetorno.getLocalidade());
        eventoAtualizado.setUf(viaCepRetorno.getUf());
      }
      
      return paraEventoDTO(eventoRepositorio.save(eventoAtualizado));
    }
    catch (Exception e)
    {
      throw new AtualizarEventoException();
    }
  }

  public void deletarEventoPorID(String id) 
  {
    Evento evento = eventoRepositorio.findById(id).orElseThrow(EventoNaoEncontradoException::new);
    Map<String, Object> ingressos = integracaoIngresso.verificarIngressos(id);
    if (ingressos != null && Boolean.TRUE.equals(ingressos.get("existemIngressos"))) 
    {
      throw new DeletarEventoException();
    }

    try 
    {
      eventoRepositorio.delete(evento);
    } 
    catch (Exception e) 
    {
      throw new DeletarEventoException();
    }
  }
  
  private EventoDTO paraEventoDTO(Evento evento)
  {
    return new EventoDTO
    (
      evento.getId(),
      evento.getDataHora(),
      evento.getNomeEvento(),
      evento.getCep(),
      evento.getLogradouro(),
      evento.getBairro(),
      evento.getCidade(),
      evento.getUf(),
      evento.getDescricao()
    );
  }
}