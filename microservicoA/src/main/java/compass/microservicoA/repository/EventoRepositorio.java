package compass.microservicoA.repository;

import compass.microservicoA.entity.Evento;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepositorio extends MongoRepository<Evento, String> 
{
  List<Evento> findAllByOrderByNomeEventoAsc();
}