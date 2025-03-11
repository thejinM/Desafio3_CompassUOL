package compass.microservicoB.repository;

import compass.microservicoB.entity.Ingresso;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngressoRepositorio extends MongoRepository<Ingresso, String>
{
  List<Ingresso> findByCpf(String cpf);
  List<Ingresso> findByEventoID(String eventoID);
}