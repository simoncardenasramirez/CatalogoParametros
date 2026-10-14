package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheMetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config.RedisCacheProperties;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis.MetadatoCacheMapper.MetadatoCacheData;

@Repository
public class RedisMetadatoRepository extends RedisCatalogoRepository<MetadatoEntity, MetadatoCacheData>
        implements CacheMetadatoRepository {
    public RedisMetadatoRepository(final StringRedisTemplate redis, final RedisCacheProperties properties) {
        super(redis, properties, "metadato", "metadato", MetadatoCacheData[].class,
                MetadatoCacheMapper.INSTANCE::toEntity, MetadatoCacheMapper.INSTANCE::toData);
    }
}
