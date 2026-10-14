package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoMetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheTipoMetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config.RedisCacheProperties;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis.TipoMetadatoCacheMapper.TipoMetadatoCacheData;

@Repository
public class RedisTipoMetadatoRepository extends RedisCatalogoRepository<TipoMetadatoEntity, TipoMetadatoCacheData>
        implements CacheTipoMetadatoRepository {
    public RedisTipoMetadatoRepository(final StringRedisTemplate redis, final RedisCacheProperties properties) {
        super(redis, properties, "tipometadato", "tipometadato", TipoMetadatoCacheData[].class,
                TipoMetadatoCacheMapper.INSTANCE::toEntity, TipoMetadatoCacheMapper.INSTANCE::toData);
    }
}
