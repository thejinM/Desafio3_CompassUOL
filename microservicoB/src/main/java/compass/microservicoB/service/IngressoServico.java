package compass.microservicoB.service;

import compass.microservicoB.dto.IngressoDTO;
import compass.microservicoB.entity.EventoIngresso;
import compass.microservicoB.entity.Ingresso;
import compass.microservicoB.integracao.IntegracaoEvento;
import compass.microservicoB.repository.IngressoRepositorio;
import feign.FeignException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
      throw new RuntimeException("Nenhum ingresso encontrado!");
    }
    return ingressos.stream().map(this::paraIngressoDTO).collect(Collectors.toList());
  }

  public IngressoDTO buscarIngressoPorID(String id) 
  {
    return ingressoRepositorio.findById(id).map(this::paraIngressoDTO).orElseThrow(() -> new RuntimeException("Ingresso não encontrado!"));
  }

  public List<IngressoDTO> buscarIngressosPorEventoID(String eventoID) 
  {
    List<Ingresso> ingressos = ingressoRepositorio.findByEventoID(eventoID);
    if (ingressos.isEmpty()) 
    {
      throw new RuntimeException("Nenhum ingresso encontrado para o evento!");
    }
    return ingressos.stream().map(this::paraIngressoDTO).collect(Collectors.toList());
  }

  public List<IngressoDTO> buscarIngressosPorCPF(String cpf) 
  {
    List<Ingresso> ingressos = ingressoRepositorio.findByCpf(cpf);
    if (ingressos.isEmpty()) 
    {
      throw new RuntimeException("Nenhum ingresso encontrado para o CPF informado!");
    }
    return ingressos.stream().map(this::paraIngressoDTO).collect(Collectors.toList());
  }

  public IngressoDTO criarIngresso(IngressoDTO DTO) 
  {
    if (DTO.getEventoID() == null || DTO.getEventoID().isBlank()) 
    {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O eventoID é obrigatório.");
    }

    try 
    {
      EventoIngresso evento = integracaoEvento.buscarEventoPorID(DTO.getEventoID());

      if (evento == null) 
      {
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "O evento informado não existe.");
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
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "O evento informado não existe.");
    }
    catch (Exception e) 
    {
      throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao criar ingresso.", e);
    }
  }

  public IngressoDTO atualizarIngressoPorID(String id, IngressoDTO DTO) 
  {
    try 
    {
      Ingresso ingressoAtualizado = ingressoRepositorio.findById(id).orElseThrow(() -> new RuntimeException("Ingresso não encontrado!"));

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
      throw new RuntimeException("Erro ao atualizar ingresso!", e);
    }
  }

  public void deletarIngressoPorID(String id) 
  {
    Ingresso ingresso = ingressoRepositorio.findById(id).orElseThrow(() -> new RuntimeException("Ingresso não encontrado!"));
    try 
    {
      ingressoRepositorio.delete(ingresso);
    } 
    catch (Exception e) 
    {
      throw new RuntimeException("Erro ao deletar ingresso!", e);
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