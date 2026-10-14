package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;

@Component
@ConfigurationProperties(prefix = "catalogo.cache")
public class RedisCacheProperties {
    private boolean habilitada = true;
    private Duration ttl = Duration.ofMinutes(5);
    private String prefijo = "catalogo-parametros";

    public boolean isHabilitada() {
        return habilitada;
    }

    public void setHabilitada(final boolean habilitada) {
        this.habilitada = habilitada;
    }

    public Duration getTtl() {
        return ttl;
    }

    public void setTtl(final Duration ttl) {
        if (ttl == null || ttl.isZero() || ttl.isNegative()) {
            throw new TechnicalException("El TTL de la cache debe ser positivo.");
        }
        this.ttl = ttl;
    }

    public String getPrefijo() {
        return prefijo;
    }

    public void setPrefijo(final String prefijo) {
        if (prefijo == null || prefijo.isBlank()) {
            throw new TechnicalException("El prefijo de la cache es obligatorio.");
        }
        this.prefijo = prefijo;
    }
}
