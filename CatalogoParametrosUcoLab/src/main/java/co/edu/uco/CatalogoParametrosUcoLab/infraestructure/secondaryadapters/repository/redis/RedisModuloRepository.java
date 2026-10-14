package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config.RedisCacheProperties;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis.ModuloCacheMapper.ModuloCacheData;

@Repository
public class RedisModuloRepository extends RedisCatalogoRepository<ModuloEntity, ModuloCacheData>
        implements CacheModuloRepository {
    public RedisModuloRepository(final StringRedisTemplate redis, final RedisCacheProperties properties) {
        super(redis, properties, "modulo", "modulo", ModuloCacheData[].class,
                ModuloCacheMapper.INSTANCE::toEntity, ModuloCacheMapper.INSTANCE::toData);
    }
}
