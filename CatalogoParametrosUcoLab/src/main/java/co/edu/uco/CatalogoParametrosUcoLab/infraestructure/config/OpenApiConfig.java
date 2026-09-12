package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI catalogoParametrosOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Catálogo de Parámetros UcoLab")
                .description("API para gestionar el catálogo de parámetros de UcoLab.")
                .version("v1"));
    }
}
