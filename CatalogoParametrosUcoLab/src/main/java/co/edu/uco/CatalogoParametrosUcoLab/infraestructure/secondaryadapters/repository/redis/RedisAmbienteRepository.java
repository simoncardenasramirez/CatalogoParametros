package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import org.springframework.data.redis.core.StringRedisTemplate;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config.RedisCacheProperties;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis.AmbienteCacheMapper.AmbienteCacheData;

public class RedisAmbienteRepository extends RedisCatalogoRepository<AmbienteEntity, AmbienteCacheData>
        implements CacheAmbienteRepository {
    public RedisAmbienteRepository(final StringRedisTemplate redis, final RedisCacheProperties properties) {
        super(redis, properties, "ambiente", "ambiente", AmbienteCacheData[].class,
                AmbienteCacheMapper.INSTANCE::toEntity, AmbienteCacheMapper.INSTANCE::toData);
    }
}
