package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config.RedisCacheProperties;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis.AmbienteCacheMapper.AmbienteCacheData;

@Repository
public class RedisAmbienteRepository extends RedisCatalogoRepository<AmbienteEntity, AmbienteCacheData>
        implements CacheAmbienteRepository {
    public RedisAmbienteRepository(final StringRedisTemplate redis, final RedisCacheProperties properties) {
        super(redis, properties, "ambiente", "ambiente", AmbienteCacheData[].class,
                AmbienteCacheMapper.INSTANCE::toEntity, AmbienteCacheMapper.INSTANCE::toData);
    }
}
