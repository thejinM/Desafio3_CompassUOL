package compass.microservicoA.controller;

import compass.microservicoA.dto.EventoDTO;
import compass.microservicoA.exception.EventoNaoEncontradoException;
import compass.microservicoA.service.EventoServico;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "CONTROLADOR DE EVENTOS: ")
@RestController
@CrossOrigin(origins = "https://editor.swagger.io")
@RequestMapping(value = "/api/eventos")
public class EventoControlador 
{
  @Autowired
  private EventoServico eventoServico;

  @Operation(summary = "Busca todos os eventos.", responses = 
  {
    @ApiResponse(responseCode = "200", description = "Todos os eventos foram encontrados com sucesso!", content = @Content(schema = @Schema(implementation = EventoDTO.class))),
    @ApiResponse(responseCode = "204", description = "Nenhum evento foi encontrado!")
  })
  @GetMapping("/buscaTodosEventos")
  public ResponseEntity<List<EventoDTO>> buscaTodosEventos() 
  {
    List<EventoDTO> eventos = eventoServico.buscarEventos();
    if (eventos.isEmpty()) 
    {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
    }
    return ResponseEntity.ok().body(eventos); 
  }

  @Operation(summary = "Busca um evento pelo seu ID.", responses = 
  {
    @ApiResponse(responseCode = "200", description = "Evento encontrado com sucesso!", content = @Content(schema = @Schema(implementation = EventoDTO.class))),
    @ApiResponse(responseCode = "400", description = "Requisição inválida. O ID não pode ser nulo ou vazio."),
    @ApiResponse(responseCode = "404", description = "Nenhum evento foi encontrado!")
  })
  @GetMapping("/buscaEventoPorID/{id}")
  public ResponseEntity<EventoDTO> buscaEventoPorID(@PathVariable String id) 
  {
    if (id == null || id.trim().isEmpty()) 
    {
      return ResponseEntity.badRequest().build();
    }
  
    try 
    {
      EventoDTO eventoDTO = eventoServico.buscarEventoPorID(id);
      return ResponseEntity.ok(eventoDTO);
    } 
    catch (EventoNaoEncontradoException e) 
    {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }

  @Operation(summary = "Lista todos os eventos em ordem alfabética.", responses = 
  {
    @ApiResponse(responseCode = "200", description = "Eventos listados com sucesso!", content = @Content(schema = @Schema(implementation = EventoDTO.class))),
    @ApiResponse(responseCode = "204", description = "Nenhum evento encontrado!")
  })
  @GetMapping("/listarEventosAlfabeticamente")
  public ResponseEntity<List<EventoDTO>> eventosOrdenados() 
  {
    List<EventoDTO> eventos = eventoServico.buscarEventosOrdenados();
    if (eventos.isEmpty()) 
    {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    return ResponseEntity.ok().body(eventos);
  }

  @Operation(summary = "Cria um evento.", responses = 
  {
    @ApiResponse(responseCode = "201", description = "Evento criado com sucesso!", content = @Content(schema = @Schema(implementation = EventoDTO.class))),
    @ApiResponse(responseCode = "400", description = "Requisição inválida. Campos obrigatórios podem estar ausentes ou com formato incorreto.")
  })
  @PostMapping("/criaEvento")
  public ResponseEntity<EventoDTO> criarEvento(@RequestBody EventoDTO eventoDTO) 
  {
    if (eventoDTO == null) 
    {
      return ResponseEntity.badRequest().build();
    }
    EventoDTO eventoCriado = eventoServico.criarEvento(eventoDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(eventoCriado);
  }

  @Operation(summary = "Atualiza um evento pelo seu ID.", responses = 
  {
    @ApiResponse(responseCode = "200", description = "Evento atualizado com sucesso!", content = @Content(schema = @Schema(implementation = EventoDTO.class))),
    @ApiResponse(responseCode = "400", description = "Requisição inválida. O ID não pode ser nulo ou vazio."),
    @ApiResponse(responseCode = "404", description = "Evento não encontrado!"),
    @ApiResponse(responseCode = "500", description = "Erro interno ao atualizar o evento.")
  })
  @PutMapping("/atualizaEventoPorID/{id}")
  public ResponseEntity<EventoDTO> atualizarEventoPorID(@PathVariable String id, @RequestBody @Valid EventoDTO eventoDTO) 
  {
    if (id == null || id.isBlank()) 
    {
      return ResponseEntity.badRequest().build();
    }
  
    try 
    {
      EventoDTO eventoAtualizado = eventoServico.atualizarEventoPorID(id, eventoDTO);
      return ResponseEntity.ok(eventoAtualizado);
    } 
    catch (EventoNaoEncontradoException e) 
    {
      return ResponseEntity.notFound().build();
    }
    catch (Exception e) 
    {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }  

  @Operation(summary = "Deleta um evento pelo seu ID.", responses = 
  {
    @ApiResponse(responseCode = "204", description = "Evento deletado com sucesso!"),
    @ApiResponse(responseCode = "400", description = "Requisição inválida. O ID não pode ser nulo ou vazio."),
    @ApiResponse(responseCode = "404", description = "Evento não encontrado."),
    @ApiResponse(responseCode = "409", description = "Evento não pode ser excluído, pois há ingressos vinculados a ele.")
  })
  @DeleteMapping("/deletaEventoPorID/{id}")
  public ResponseEntity<Void> deletarEventoPorID(@PathVariable String id) 
  {
    if (id == null || id.trim().isEmpty()) 
    {
      return ResponseEntity.badRequest().build();
    }
  
    try 
    {
      eventoServico.deletarEventoPorID(id);
      return ResponseEntity.noContent().build();
    } 
    catch (EventoNaoEncontradoException e) 
    {
      return ResponseEntity.notFound().build();
    }
  }  
}