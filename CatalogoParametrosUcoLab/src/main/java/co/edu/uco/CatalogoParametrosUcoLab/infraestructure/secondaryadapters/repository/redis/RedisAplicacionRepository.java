package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheAplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config.RedisCacheProperties;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis.AplicacionCacheMapper.AplicacionCacheData;

@Repository
public class RedisAplicacionRepository extends RedisCatalogoRepository<AplicacionEntity, AplicacionCacheData>
        implements CacheAplicacionRepository {
    public RedisAplicacionRepository(final StringRedisTemplate redis, final RedisCacheProperties properties) {
        super(redis, properties, "aplicacion", "aplicacion", AplicacionCacheData[].class,
                AplicacionCacheMapper.INSTANCE::toEntity, AplicacionCacheMapper.INSTANCE::toData);
    }
}
