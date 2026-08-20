package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.reactive.config.CorsRegistry;

class WebConfigTest {

    private static final class CorsRegistryAccesible extends CorsRegistry {

        Map<String, CorsConfiguration> getConfiguraciones() {
            return getCorsConfigurations();
        }
    }

    @Test
    void debeConfigurarElMapeoCorsParaLocalhost4200ConLosMetodosPermitidos() {
        var registry = new CorsRegistryAccesible();

        new WebConfig().addCorsMappings(registry);

        var config = registry.getConfiguraciones().get("/**");
        assertNotNull(config);
        assertEquals(List.of("http://localhost:4200"), config.getAllowedOrigins());
        assertEquals(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"), config.getAllowedMethods());
        assertEquals(List.of("*"), config.getAllowedHeaders());
        assertTrue(config.getAllowCredentials());
        assertEquals(3600L, config.getMaxAge());
    }
}