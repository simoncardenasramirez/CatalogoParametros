package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheEstadoMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config.RedisCacheProperties;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis.EstadoMetadatoAmbienteCacheMapper.EstadoMetadatoAmbienteCacheData;

@Repository
public class RedisEstadoMetadatoAmbienteRepository extends RedisCatalogoRepository<EstadoMetadatoAmbienteEntity, EstadoMetadatoAmbienteCacheData>
        implements CacheEstadoMetadatoAmbienteRepository {
    public RedisEstadoMetadatoAmbienteRepository(final StringRedisTemplate redis, final RedisCacheProperties properties) {
        super(redis, properties, "estadometadatoambiente", "estadometadatoambiente", EstadoMetadatoAmbienteCacheData[].class,
                EstadoMetadatoAmbienteCacheMapper.INSTANCE::toEntity, EstadoMetadatoAmbienteCacheMapper.INSTANCE::toData);
    }
}
