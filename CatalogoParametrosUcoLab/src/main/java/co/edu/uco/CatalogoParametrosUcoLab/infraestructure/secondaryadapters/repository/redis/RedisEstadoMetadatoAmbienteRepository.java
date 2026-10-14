package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheEstadoMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config.RedisCacheProperties;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis.EstadoMetadatoAmbienteCacheMapper.EstadoMetadatoAmbienteCacheData;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

@Repository
public class RedisEstadoMetadatoAmbienteRepository implements CacheEstadoMetadatoAmbienteRepository {
    private final StringRedisTemplate redis;
    private final JsonMapper mapper;
    private final Duration ttl;
    private final String prefijo;
    private final boolean habilitada;

    public RedisEstadoMetadatoAmbienteRepository(final StringRedisTemplate redis, final RedisCacheProperties properties) {
        this.redis = redis;
        this.mapper = JsonMapper.builder().build();
        this.ttl = properties.getTtl();
        this.prefijo = properties.getPrefijo() + ":estadometadatoambiente:v1:";
        this.habilitada = properties.isHabilitada();
    }

    @Override
    public String obtenerVersion() {
        if (!habilitada) {
            return "deshabilitada";
        }
        return ejecutar(() -> {
            var version = redis.opsForValue().get(prefijo + "version");
            if (version == null) {
                var nuevaVersion = UUID.randomUUID().toString();
                redis.opsForValue().setIfAbsent(prefijo + "version", nuevaVersion);
                version = redis.opsForValue().get(prefijo + "version");
            }
            if (version == null) {
                throw new TechnicalException("No fue posible obtener la version de cache de estadometadatoambiente.");
            }
            return version;
        });
    }

    @Override
    public Optional<List<EstadoMetadatoAmbienteEntity>> consultar(final String version, final String clave) {
        if (!habilitada) {
            return Optional.empty();
        }
        return ejecutar(() -> {
            var contenido = redis.opsForValue().get(prefijo + version + ":" + clave);
            if (contenido == null) {
                return Optional.empty();
            }
            var datos = mapper.readValue(contenido, EstadoMetadatoAmbienteCacheData[].class);
            return Optional.of(Arrays.stream(datos).map(EstadoMetadatoAmbienteCacheMapper.INSTANCE::toEntity).toList());
        });
    }

    @Override
    public void guardar(final String version, final String clave, final List<EstadoMetadatoAmbienteEntity> estadometadatoambiente) {
        if (!habilitada) {
            return;
        }
        ejecutar(() -> {
            var datos = estadometadatoambiente.stream().map(EstadoMetadatoAmbienteCacheMapper.INSTANCE::toData).toList();
            redis.opsForValue().set(prefijo + version + ":" + clave, mapper.writeValueAsString(datos), ttl);
            return null;
        });
    }

    @Override
    public void invalidar() {
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
        } catch (final DataAccessException | JacksonException exception) {
            throw new TechnicalException("Error al acceder a la cache Redis de estadometadatoambiente.", exception);
        }
    }
}
