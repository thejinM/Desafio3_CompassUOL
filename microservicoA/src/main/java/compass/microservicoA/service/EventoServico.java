package compass.microservicoA.service;

import compass.microservicoA.dto.EventoDTO;
import compass.microservicoA.entity.Evento;
import compass.microservicoA.integracao.IntegracaoIngresso;
import compass.microservicoA.integracao.ViaCEP;
import compass.microservicoA.integracao.ViaCEPResposta;
import compass.microservicoA.repository.EventoRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
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
      throw new RuntimeException("Nenhum evento encontrado!");
    }

    return eventos.stream().map(this::paraEventoDTO).collect(Collectors.toList());
  }

  public EventoDTO buscarEventoPorID(String id)
  {
    return eventoRepositorio.findById(id).map(this::paraEventoDTO).orElseThrow(() -> new RuntimeException("Evento não encontrado!"));
  }

  public List<EventoDTO> buscarEventosOrdenados() 
  {
    List<Evento> eventos = eventoRepositorio.findAllByOrderByNomeEventoAsc();

    if (eventos.isEmpty()) 
    {
      throw new RuntimeException("Nenhum evento encontrado!");
    }
  
    return eventos.stream().map(this::paraEventoDTO).collect(Collectors.toList());
  }

  public EventoDTO criarEvento(EventoDTO dto)
  {
    try
    {
      ViaCEPResposta viaCep = viaCEP.consultarCEP(dto.getCep());
      Evento evento = new Evento();

      evento.setDataHora(dto.getDataHora());
      evento.setNomeEvento(dto.getNomeEvento());
      evento.setCep(dto.getCep());
      evento.setLogradouro(viaCep.getLogradouro());
      evento.setBairro(viaCep.getBairro());
      evento.setCidade(viaCep.getLocalidade());
      evento.setUf(viaCep.getUf());
      evento.setDescricao(dto.getDescricao());

      return paraEventoDTO(eventoRepositorio.save(evento));
    }
    catch (Exception e)
    {
      throw new RuntimeException("Erro ao criar evento!");
    }
  }

  public EventoDTO atualizarEventoPorID(String id, EventoDTO eventoDTO)
  {
    try 
    {
      Evento eventoAtualizado = eventoRepositorio.findById(id).orElseThrow(() -> new RuntimeException("Evento não encontrado!"));

      eventoAtualizado.setNomeEvento(eventoDTO.getNomeEvento());
      eventoAtualizado.setDataHora(eventoDTO.getDataHora());
      
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
      throw new RuntimeException("Erro ao atualizar evento!");
    }
  }

  public void deletarEventoPorID(String eventoID) 
  {
    boolean existemIngressos = Boolean.TRUE.equals(integracaoIngresso.verificarIngressos(eventoID).get("existemIngressos"));

    if (existemIngressos) 
    {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "O evento não pode ser deletado porque possui ingressos vendidos!");
    }

    eventoRepositorio.deleteById(eventoID);
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