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
  @GetMapping("/todosEventos")
  public ResponseEntity<List<EventoDTO>> getTodosEventos() 
  {
    List<EventoDTO> eventos = eventoServico.buscarEventos();
    if (eventos.isEmpty()) 
    {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
    }
    return ResponseEntity.ok().body(eventos); 
  }

  @Operation(summary = "Busca um evento por ID.", responses = 
  {
    @ApiResponse(responseCode = "200", description = "Evento encontrado com sucesso!", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = EventoDTO.class))),
  })
  @GetMapping("/{id}")
  public ResponseEntity<EventoDTO> getBuscaEventoPorId(@PathVariable String id) 
  {
    EventoDTO evento = eventoServico.buscarEventoPorID(id);
    if (evento == null) 
    {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
    }
    return ResponseEntity.ok(evento); 
  }

  @Operation(summary = "Cria um evento.", responses = 
  {
    @ApiResponse(responseCode = "201", description = "Evento criado com sucesso!", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = EventoDTO.class))),
  })
  @PostMapping
  public ResponseEntity<EventoDTO> criarEvento(@RequestBody EventoDTO dto) 
  {
    EventoDTO eventoCriado = new EventoDTO
    (
      null, 
      dto.getDataHora(),
      dto.getNomeEvento(),
      dto.getCep(),
      dto.getLogradouro(),
      dto.getBairro(),
      dto.getCidade(),
      dto.getUf(),
      dto.getDescricao()
    );
  
    eventoCriado = eventoServico.criarEvento(eventoCriado);  
    EventoDTO eventoDTO = eventoCriado;
    return ResponseEntity.status(HttpStatus.CREATED).body(eventoDTO); 
  }

  @Operation(summary = "Atualizar evento.", responses = 
  {
    @ApiResponse(responseCode = "204", description = "Evento atualizado com sucesso!", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = EventoDTO.class))),
    @ApiResponse(responseCode = "404", description = "Evento não encontrado!", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = RuntimeException.class))),
  })
  @PutMapping("/{id}")
  public ResponseEntity<EventoDTO> atualizarEvento(@PathVariable String id, @RequestBody EventoDTO eventoDTO) 
  {
    EventoDTO eventoAtualizado = eventoServico.atualizarEventoPorID(id, eventoDTO); 
    if (eventoAtualizado == null) 
    {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); 
    }
    return ResponseEntity.status(HttpStatus.OK).body(eventoAtualizado);
  }

  @Operation(summary = "Deletar evento.", responses = 
  {
    @ApiResponse(responseCode = "204", description = "Evento deletado com sucesso!", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = EventoDTO.class))),
    @ApiResponse(responseCode = "404", description = "Evento não encontrado.", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = RuntimeException.class))),
  })
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> deletarEventoPorId(@PathVariable String id) 
  {
    eventoServico.deletarEventoPorID(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
  }
}