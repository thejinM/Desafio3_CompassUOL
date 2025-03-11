package compass.microservicoA.controller;

import compass.microservicoA.dto.EventoDTO;
import compass.microservicoA.service.EventoServico;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://editor.swagger.io")
@RequestMapping(value = "/api/eventos")
public class EventoControlador 
{
  @Autowired
  private EventoServico eventoServico;

  @Operation(summary = "Busca todos os eventos.", responses = 
  {
    @ApiResponse(responseCode = "200", description = "Todos os eventos foram encontrados com sucesso!"),
    @ApiResponse(responseCode = "404", description = "Nenhum evento foi encontrado!", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = EventoDTO.class))),
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
    @ApiResponse(responseCode = "200", description = "Evento encontrado com sucesso!", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = EventoDTO.class))),
  })
  @GetMapping("/buscaEventoPorID/{id}")
  public ResponseEntity<EventoDTO> buscaEventoPorID(@PathVariable String id) 
  {
    EventoDTO eventoDTO = eventoServico.buscarEventoPorID(id);
    if (eventoDTO == null) 
    {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
    }
    return ResponseEntity.ok(eventoDTO); 
  }

  @Operation(summary = "Lista todos os eventos em ordem alfabética.", responses = 
  {
    @ApiResponse(responseCode = "200", description = "Eventos listados com sucesso!"),
    @ApiResponse(responseCode = "404", description = "Nenhum evento encontrado!", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = EventoDTO.class))),
  })
  @GetMapping("/listarEventosAlfabéticamente")
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
    @ApiResponse(responseCode = "201", description = "Evento criado com sucesso!", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = EventoDTO.class))),
  })
  @PostMapping("/criaEvento")
  public ResponseEntity<EventoDTO> criarEvento(@RequestBody EventoDTO DTO) 
  {
    EventoDTO eventoCriado = eventoServico.criarEvento(DTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(eventoCriado); 
  }

  @Operation(summary = "Atualiza um evento pelo seu ID.", responses = 
  {
    @ApiResponse(responseCode = "204", description = "Evento atualizado com sucesso!", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = EventoDTO.class))),
    @ApiResponse(responseCode = "404", description = "Evento não encontrado!", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = RuntimeException.class))),
  })
  @PutMapping("/atualizaEventoPorID/{id}")
  public ResponseEntity<EventoDTO> atualizarEventoPorID(@PathVariable String id, @RequestBody EventoDTO eventoDTO) 
  {
    EventoDTO eventoAtualizado = eventoServico.atualizarEventoPorID(id, eventoDTO); 
    if (eventoAtualizado == null) 
    {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
    }
    return ResponseEntity.status(HttpStatus.OK).body(eventoAtualizado);
  }

  @Operation(summary = "Deleta um evento pelo seu ID.", responses = 
  {
    @ApiResponse(responseCode = "204", description = "Evento deletado com sucesso!", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = EventoDTO.class))),
    @ApiResponse(responseCode = "404", description = "Evento não encontrado.", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = RuntimeException.class))),
  })
  @DeleteMapping("/deletaEventoPorID/{id}")
  public ResponseEntity<Void> deletarEventoPorID(@PathVariable String id) 
  {
    EventoDTO evento = eventoServico.buscarEventoPorID(id);
    eventoServico.deletarEventoPorID(id);
  
    if (evento == null)
    {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
    }    
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
  }
}