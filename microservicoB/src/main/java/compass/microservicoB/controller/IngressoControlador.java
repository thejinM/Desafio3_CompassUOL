package compass.microservicoB.controller;

import compass.microservicoB.dto.IngressoDTO;
import compass.microservicoB.service.IngressoServico;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "https://editor.swagger.io")
@RequestMapping(value = "/api/ingressos")
public class IngressoControlador 
{
  @Autowired
  private IngressoServico ingressoServico;

  @Operation(summary = "Busca todos os ingressos.", responses = 
  {
      @ApiResponse(responseCode = "200", description = "Todos os ingressos foram encontrados com sucesso!"),
      @ApiResponse(responseCode = "404", description = "Nenhum ingresso foi encontrado!", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = IngressoDTO.class))),
  })
  @GetMapping("/buscaTodosIngressos")
  public ResponseEntity<List<IngressoDTO>> buscaTodosIngressos() 
  {
    List<IngressoDTO> ingressos = ingressoServico.buscarIngressos();
    if (ingressos.isEmpty()) 
    {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
    }
    return ResponseEntity.ok().body(ingressos); 
  }

 @Operation(summary = "Busca um ingresso pelo seu ID.", responses = 
  {
    @ApiResponse(responseCode = "200", description = "Ingresso encontrado com sucesso!", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = IngressoDTO.class))),
    @ApiResponse(responseCode = "404", description = "Ingresso não encontrado."),
  })
  @GetMapping("/buscaIngressoPorID/{id}")
  public ResponseEntity<IngressoDTO> buscaIngressoPorID(@PathVariable String id) 
  {
    try 
    {
      IngressoDTO ingresso = ingressoServico.buscarIngressoPorID(id);
        
      return ResponseEntity.ok(ingresso);
    } 
    catch (ResponseStatusException e) 
    {
      return ResponseEntity.status(e.getStatusCode()).build();
    } 
    catch (Exception e) 
    {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @Operation(summary = "Busca ingressos pelo ID do evento.", responses = 
  {
    @ApiResponse(responseCode = "200", description = "Ingressos encontrados com sucesso!"),
    @ApiResponse(responseCode = "404", description = "Nenhum ingresso encontrado para o evento!", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = IngressoDTO.class))),
  })
  @GetMapping("/buscarIngressosPorEventoID/{eventoID}")
  public ResponseEntity<List<IngressoDTO>> buscarIngressosPorEventoID(@PathVariable(required = false) String eventoID) 
  {
    if (eventoID == null || eventoID.isBlank()) 
    {
      return ResponseEntity.badRequest().build();
    }
  
    List<IngressoDTO> ingressos = ingressoServico.buscarIngressosPorEventoID(eventoID);
      
    if (ingressos.isEmpty()) 
    {
      return ResponseEntity.noContent().build();
    }
  
    return ResponseEntity.ok(ingressos);
  }  
  
  @Operation(summary = "Busca ingressos pelo CPF do cliente.", responses = 
  {
    @ApiResponse(responseCode = "200", description = "Ingressos encontrados com sucesso!"),
    @ApiResponse(responseCode = "404", description = "Nenhum ingresso encontrado para o CPF!", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = IngressoDTO.class))),
  })
  @GetMapping("/buscaIngressosPorCPF/{cpf}")
  public ResponseEntity<List<IngressoDTO>> buscarIngressosPorCPF(@PathVariable String cpf) 
  {
    List<IngressoDTO> ingressos = ingressoServico.buscarIngressosPorCPF(cpf);
    if (ingressos.isEmpty()) 
    {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    return ResponseEntity.ok().body(ingressos);
  }

  @Operation(summary = "Cria um ingresso.", responses = 
  {
      @ApiResponse(responseCode = "201", description = "Ingresso criado com sucesso!", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = IngressoDTO.class))),
      @ApiResponse(responseCode = "400", description = "Requisição inválida. O eventoID é obrigatório.", content = @Content(mediaType = "application/json;charset=UTF-8")),
      @ApiResponse(responseCode = "404", description = "Evento não encontrado.", content = @Content(mediaType = "application/json;charset=UTF-8"))
  })
  @PostMapping("/criaIngresso")
  public ResponseEntity<IngressoDTO> criarIngresso(@RequestBody IngressoDTO ingressoDTO) 
  {
    IngressoDTO ingressoCriado = ingressoServico.criarIngresso(ingressoDTO);
    if (ingressoDTO == null) 
    {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(ingressoCriado); 
  }  

  @Operation(summary = "Atualiza um ingresso pelo seu ID.", responses = 
  {
      @ApiResponse(responseCode = "200", description = "Ingresso atualizado com sucesso!", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = IngressoDTO.class))),
      @ApiResponse(responseCode = "404", description = "Ingresso não encontrado!", content = @Content(mediaType = "application/json;charset=UTF-8")),
      @ApiResponse(responseCode = "400", description = "Requisição inválida. Dados fornecidos para atualização do ingresso estão incompletos ou incorretos.", content = @Content(mediaType = "application/json;charset=UTF-8")),
  })
  @PutMapping("/atualizaIngressoPorID/{id}")
  public ResponseEntity<IngressoDTO> atualizarIngressoPorID(@PathVariable String id, @RequestBody IngressoDTO ingressoDTO) 
  {
    if (id == null || id.isBlank()) 
    {
      return ResponseEntity.badRequest().build();
    }
  
    IngressoDTO ingressoAtualizado = ingressoServico.atualizarIngressoPorID(id, ingressoDTO);
  
    if (ingressoAtualizado == null) 
    {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  
    return ResponseEntity.ok(ingressoAtualizado);
  }  

  @Operation(summary = "Deleta um ingresso pelo seu ID.", responses = 
  {
    @ApiResponse(responseCode = "204", description = "Ingresso deletado com sucesso!", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = IngressoDTO.class))),
    @ApiResponse(responseCode = "404", description = "Ingresso não encontrado.", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = RuntimeException.class))),
  })
  @DeleteMapping("/deletaIngressoPorID/{id}")
  public ResponseEntity<Void> deletarIngressoPorID(@PathVariable String id) 
  {
    IngressoDTO ingresso = ingressoServico.buscarIngressoPorID(id);
      
    if (ingresso == null) 
    {
      return ResponseEntity.notFound().build();
    }
  
    ingressoServico.deletarIngressoPorID(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Verifica se existem ingressos vendidos para um evento.", responses = 
  {
    @ApiResponse(responseCode = "204", description = "Verificação realizada com sucesso!"),
    @ApiResponse(responseCode = "404", description = "Nenhum ingresso encontrado para o evento!", content = @Content(mediaType = "application/json;charset=UTF-8"))
  })
  @GetMapping("/checarIngressosPorEventoID/{eventoID}")
  public ResponseEntity<Map<String, Object>> checarIngressosPorEventoID(@PathVariable String eventoID) 
  {
    List<IngressoDTO> ingressos = ingressoServico.buscarIngressosPorEventoID(eventoID);
    if (ingressos.isEmpty()) 
    {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("eventoID", eventoID, "existemIngressos", false));
    }
    return ResponseEntity.ok().body(Map.of("eventoID", eventoID, "existemIngressos", true));
  }
}