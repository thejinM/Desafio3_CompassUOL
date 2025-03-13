package compass.microservicoB.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class ConfiguracaoSwaggerIngressos
{
  @Bean
  public OpenAPI customOpenAPI() 
  {
    return new OpenAPI()
      .info(new Info().title("API DE INGRESSOS")
      .version("1.0.0")
      .description("Uma API que gerencia todos os seus ingressos de forma fácil!"));
  }
}