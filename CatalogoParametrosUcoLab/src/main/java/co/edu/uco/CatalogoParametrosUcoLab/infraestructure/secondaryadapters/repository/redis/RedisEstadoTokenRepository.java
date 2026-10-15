package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoTokenEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheEstadoTokenRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config.RedisCacheProperties;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis.EstadoTokenCacheMapper.EstadoTokenCacheData;

@Repository
public class RedisEstadoTokenRepository extends RedisCatalogoRepository<EstadoTokenEntity, EstadoTokenCacheData>
        implements CacheEstadoTokenRepository {
    public RedisEstadoTokenRepository(final StringRedisTemplate redis, final RedisCacheProperties properties) {
        super(redis, properties, "estadotoken", "estadotoken", EstadoTokenCacheData[].class,
                EstadoTokenCacheMapper.INSTANCE::toEntity, EstadoTokenCacheMapper.INSTANCE::toData);
    }
}
