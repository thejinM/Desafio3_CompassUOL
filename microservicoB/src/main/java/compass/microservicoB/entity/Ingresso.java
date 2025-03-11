package compass.microservicoB.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ingressos")
public class Ingresso implements Serializable
{
  @Id
  private String id = UUID.randomUUID().toString(); 

  private String cpf;
  private String nomeCliente;
  private String emailCliente;
  private String eventoID; 
  private EventoIngresso evento;
  private BigDecimal valorTotalBRL;
  private BigDecimal valorTotalUSD;
  private String status;
}