package compass.microservicoB.service;

import compass.microservicoB.dto.IngressoDTO;
import compass.microservicoB.entity.EventoIngresso;
import compass.microservicoB.entity.Ingresso;
import compass.microservicoB.exception.AtualizarIngressoException;
import compass.microservicoB.exception.CriarIngressoException;
import compass.microservicoB.exception.DeletarIngressoException;
import compass.microservicoB.exception.EventoIDObrigatorioException;
import compass.microservicoB.exception.EventoNaoEncontradoException;
import compass.microservicoB.exception.IngressoNaoEncontradoException;
import compass.microservicoB.integracao.IntegracaoEvento;
import compass.microservicoB.repository.IngressoRepositorio;
import feign.FeignException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class IngressoServico 
{
  @Autowired
  private IngressoRepositorio ingressoRepositorio;

  @Autowired
  private IntegracaoEvento integracaoEvento;

  private static final BigDecimal conversao = new BigDecimal("5.83");

  public List<IngressoDTO> buscarIngressos() 
  {
    List<Ingresso> ingressos = ingressoRepositorio.findAll();
    if (ingressos.isEmpty()) 
    {
      throw new IngressoNaoEncontradoException();
    }
    return ingressos.stream().map(this::paraIngressoDTO).collect(Collectors.toList());
  }

  public IngressoDTO buscarIngressoPorID(String id) 
  {
    return ingressoRepositorio.findById(id).map(this::paraIngressoDTO).orElseThrow(() -> new IngressoNaoEncontradoException());
  }

  public List<IngressoDTO> buscarIngressosPorEventoID(String eventoID) 
  {
    List<Ingresso> ingressos = ingressoRepositorio.findByEventoID(eventoID);
    if (ingressos.isEmpty()) 
    {
      throw new IngressoNaoEncontradoException();
    }
    return ingressos.stream().map(this::paraIngressoDTO).collect(Collectors.toList());
  }

  public List<IngressoDTO> buscarIngressosPorCPF(String cpf) 
  {
    List<Ingresso> ingressos = ingressoRepositorio.findByCpf(cpf);
    if (ingressos.isEmpty()) 
    {
      throw new IngressoNaoEncontradoException();
    }
    return ingressos.stream().map(this::paraIngressoDTO).collect(Collectors.toList());
  }

  public IngressoDTO criarIngresso(IngressoDTO DTO) 
  {
    if (DTO.getEventoID() == null || DTO.getEventoID().isBlank()) 
    {
      throw new EventoIDObrigatorioException();
    }

    try 
    {
      EventoIngresso evento = integracaoEvento.buscarEventoPorID(DTO.getEventoID());

      if (evento == null) 
      {
        throw new EventoNaoEncontradoException();
      }

      Ingresso ingresso = new Ingresso();
      ingresso.setId(UUID.randomUUID().toString());
      ingresso.setCpf(DTO.getCpf());
      ingresso.setNomeCliente(DTO.getNomeCliente());
      ingresso.setEmailCliente(DTO.getEmailCliente());
      ingresso.setEventoID(DTO.getEventoID());
      ingresso.setValorTotalBRL(DTO.getValorTotalBRL());
      ingresso.setStatus("Confirmado!");

      calcularValores(ingresso); 

      return paraIngressoDTO(ingressoRepositorio.save(ingresso));
    } 
    catch (FeignException.NotFound e) 
    {
      throw new EventoNaoEncontradoException();
    }
    catch (Exception e) 
    {
      throw new CriarIngressoException(e);
    }
  }

  public IngressoDTO atualizarIngressoPorID(String id, IngressoDTO DTO) 
  {
    try 
    {
      Ingresso ingressoAtualizado = ingressoRepositorio.findById(id).orElseThrow(() -> new IngressoNaoEncontradoException());

      ingressoAtualizado.setCpf(DTO.getCpf());
      ingressoAtualizado.setNomeCliente(DTO.getNomeCliente());
      ingressoAtualizado.setEmailCliente(DTO.getEmailCliente());
      ingressoAtualizado.setEventoID(DTO.getEventoID());
      ingressoAtualizado.setValorTotalBRL(DTO.getValorTotalBRL());
      ingressoAtualizado.setStatus("Confirmado!");

      calcularValores(ingressoAtualizado); 

      return paraIngressoDTO(ingressoRepositorio.save(ingressoAtualizado));
    } 
    catch (Exception e) 
    {
      throw new AtualizarIngressoException(e);
    }
  }

  public void deletarIngressoPorID(String id) 
  {
    Ingresso ingresso = ingressoRepositorio.findById(id).orElseThrow(() -> new IngressoNaoEncontradoException());
    try 
    {
      ingressoRepositorio.delete(ingresso);
    } 
    catch (Exception e) 
    {
      throw new DeletarIngressoException(e);
    }
  }

  private void calcularValores(Ingresso ingresso) 
  {
    if (ingresso.getValorTotalBRL() != null) 
    {
      BigDecimal valorConvertido = ingresso.getValorTotalBRL().divide(conversao, 2, RoundingMode.HALF_UP);
      ingresso.setValorTotalUSD(valorConvertido);
    }
  }

  private IngressoDTO paraIngressoDTO(Ingresso ingresso) 
  {
    EventoIngresso evento = integracaoEvento.buscarEventoPorID(ingresso.getEventoID());

    return new IngressoDTO
    (
      ingresso.getId(),
      ingresso.getCpf(),
      ingresso.getNomeCliente(),
      ingresso.getEmailCliente(),
      ingresso.getEventoID(),  
      evento, 
      ingresso.getValorTotalBRL(),
      ingresso.getValorTotalUSD(),
      ingresso.getStatus()
    );
  }
}