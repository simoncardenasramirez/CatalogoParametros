package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheEstadoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config.RedisCacheProperties;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis.EstadoAmbienteCacheMapper.EstadoAmbienteCacheData;

@Repository
public class RedisEstadoAmbienteRepository extends RedisCatalogoRepository<EstadoAmbienteEntity, EstadoAmbienteCacheData>
        implements CacheEstadoAmbienteRepository {
    public RedisEstadoAmbienteRepository(final StringRedisTemplate redis, final RedisCacheProperties properties) {
        super(redis, properties, "estadoambiente", "estadoambiente", EstadoAmbienteCacheData[].class,
                EstadoAmbienteCacheMapper.INSTANCE::toEntity, EstadoAmbienteCacheMapper.INSTANCE::toData);
    }
}
