package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheFuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config.RedisCacheProperties;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis.FuncionalidadCacheMapper.FuncionalidadCacheData;

@Repository
public class RedisFuncionalidadRepository extends RedisCatalogoRepository<FuncionalidadEntity, FuncionalidadCacheData>
        implements CacheFuncionalidadRepository {
    public RedisFuncionalidadRepository(final StringRedisTemplate redis, final RedisCacheProperties properties) {
        super(redis, properties, "funcionalidad", "funcionalidad", FuncionalidadCacheData[].class,
                FuncionalidadCacheMapper.INSTANCE::toEntity, FuncionalidadCacheMapper.INSTANCE::toData);
    }
}
