package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import org.springframework.data.redis.core.StringRedisTemplate;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheTipoParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config.RedisCacheProperties;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis.TipoParametroCacheMapper.TipoParametroCacheData;

public class RedisTipoParametroRepository extends RedisCatalogoRepository<TipoParametroEntity, TipoParametroCacheData>
        implements CacheTipoParametroRepository {
    public RedisTipoParametroRepository(final StringRedisTemplate redis, final RedisCacheProperties properties) {
        super(redis, properties, "tipoparametro", "tipoparametro", TipoParametroCacheData[].class,
                TipoParametroCacheMapper.INSTANCE::toEntity, TipoParametroCacheMapper.INSTANCE::toData);
    }
}
