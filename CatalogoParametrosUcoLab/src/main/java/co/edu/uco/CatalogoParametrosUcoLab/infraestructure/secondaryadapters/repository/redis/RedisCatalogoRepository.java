package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import java.time.DateTimeException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config.RedisCacheProperties;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

/** Operaciones Redis compartidas; cada adaptador conserva su puerto y formato de datos. */
public abstract class RedisCatalogoRepository<E, D> {
    private final StringRedisTemplate redis;
    private final JsonMapper mapper;
    private final Duration ttl;
    private final String prefijo;
    private final String catalogo;
    private final boolean habilitada;
    private final Class<D[]> tipoDatos;
    private final Function<D, E> toEntity;
    private final Function<E, D> toData;

    protected RedisCatalogoRepository(final StringRedisTemplate redis, final RedisCacheProperties properties,
            final String espacioClaves, final String catalogo, final Class<D[]> tipoDatos,
            final Function<D, E> toEntity, final Function<E, D> toData) {
        this.redis = redis;
        this.mapper = JsonMapper.builder().build();
        this.ttl = properties.getTtl();
        this.prefijo = properties.getPrefijo() + ":" + espacioClaves + ":v1:";
        this.catalogo = catalogo;
        this.habilitada = properties.isHabilitada();
        this.tipoDatos = tipoDatos;
        this.toEntity = toEntity;
        this.toData = toData;
    }

    public final String obtenerVersion() {
        if (!habilitada) {
            return "deshabilitada";
        }
        return ejecutar(() -> {
            var version = redis.opsForValue().get(prefijo + "version");
            if (version == null) {
                redis.opsForValue().setIfAbsent(prefijo + "version", UUID.randomUUID().toString());
                version = redis.opsForValue().get(prefijo + "version");
            }
            if (version == null) {
                throw new TechnicalException("No fue posible obtener la version de cache de " + catalogo + ".");
            }
            return version;
        });
    }

    public final Optional<List<E>> consultar(final String version, final String clave) {
        if (!habilitada) {
            return Optional.empty();
        }
        return ejecutar(() -> {
            var contenido = redis.opsForValue().get(prefijo + version + ":" + clave);
            if (contenido == null) {
                return Optional.empty();
            }
            var datos = mapper.readValue(contenido, tipoDatos);
            return Optional.of(Arrays.stream(datos).map(toEntity).toList());
        });
    }

    public final void guardar(final String version, final String clave, final List<E> entidades) {
        if (!habilitada) {
            return;
        }
        ejecutar(() -> {
            var datos = entidades.stream().map(toData).toList();
            redis.opsForValue().set(prefijo + version + ":" + clave, mapper.writeValueAsString(datos), ttl);
            return null;
        });
    }

    public final void invalidar() {
        if (habilitada) {
            ejecutar(() -> {
                redis.opsForValue().set(prefijo + "version", UUID.randomUUID().toString());
                return null;
            });
        }
    }

    private <T> T ejecutar(final Supplier<T> operacion) {
        try {
            return operacion.get();
        } catch (final DataAccessException | JacksonException | DateTimeException exception) {
            throw new TechnicalException("Error al acceder a la cache Redis de " + catalogo + ".", exception);
        }
    }
}
