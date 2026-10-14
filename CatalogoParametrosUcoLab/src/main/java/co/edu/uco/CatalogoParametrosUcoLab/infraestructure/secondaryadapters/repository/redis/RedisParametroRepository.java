package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config.RedisCacheProperties;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis.ParametroCacheMapper.ParametroCacheData;

@Repository
public class RedisParametroRepository extends RedisCatalogoRepository<ParametroEntity, ParametroCacheData>
        implements CacheParametroRepository {
    public RedisParametroRepository(final StringRedisTemplate redis, final RedisCacheProperties properties) {
        super(redis, properties, "parametros", "parametros", ParametroCacheData[].class,
                ParametroCacheMapper.INSTANCE::toEntity, ParametroCacheMapper.INSTANCE::toData);
    }
}
