package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config.RedisCacheProperties;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis.MetadatoAmbienteCacheMapper.MetadatoAmbienteCacheData;

@Repository
public class RedisMetadatoAmbienteRepository extends RedisCatalogoRepository<MetadatoAmbienteEntity, MetadatoAmbienteCacheData>
        implements CacheMetadatoAmbienteRepository {
    public RedisMetadatoAmbienteRepository(final StringRedisTemplate redis, final RedisCacheProperties properties) {
        super(redis, properties, "metadatoambiente", "metadatoambiente", MetadatoAmbienteCacheData[].class,
                MetadatoAmbienteCacheMapper.INSTANCE::toEntity, MetadatoAmbienteCacheMapper.INSTANCE::toData);
    }
}
