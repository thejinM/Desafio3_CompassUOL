package compass.microservicoA.service;

import compass.microservicoA.dto.EventoDTO;
import compass.microservicoA.entity.Evento;
import compass.microservicoA.integracao.ViaCEP;
import compass.microservicoA.integracao.ViaCEPResposta;
import compass.microservicoA.repository.EventoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventoServico
{
  @Autowired
  private EventoRepositorio eventoRepositorio;

  @Autowired
  private ViaCEP viaCEP;

  public List<EventoDTO> buscarTodos()
  {
    List<Evento> eventos = eventoRepositorio.findAll();

    if (eventos.isEmpty())
    {
      throw new RuntimeException("Nenhum evento encontrado!");
    }

    return eventos.stream().map(this::paraDTO).collect(Collectors.toList());
  }

  public EventoDTO buscarEventoPorId(String id)
  {
    return eventoRepositorio.findById(id).map(this::paraDTO).orElseThrow(() -> new RuntimeException("Evento não encontrado!"));
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

      return paraDTO(eventoRepositorio.save(evento));
    }
    catch (Exception e)
    {
      throw new RuntimeException("Erro ao criar evento!");
    }
  }

  public EventoDTO atualizarEvento(String id, EventoDTO dto)
  {
    Evento eventoExistente = eventoRepositorio.findById(id).orElseThrow(() -> new RuntimeException("Evento não encontrado!"));

    eventoExistente.setDataHora(dto.getDataHora());
    eventoExistente.setNomeEvento(dto.getNomeEvento());
    eventoExistente.setDescricao(dto.getDescricao());
    try
    {
      return paraDTO(eventoRepositorio.save(eventoExistente));
    }
    catch (Exception e)
    {
      throw new RuntimeException("Erro ao atualizar evento!");
    }
  }

  public void deletarEvento(String id)
  {
    if (!eventoRepositorio.existsById(id))
    {
      throw new RuntimeException("Evento não encontrado!");
    }
    try
    {
      eventoRepositorio.deleteById(id);
    }
    catch (Exception e)
    {
      throw new RuntimeException("Erro ao deletar evento!");
    }
  }

  private EventoDTO paraDTO(Evento evento)
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