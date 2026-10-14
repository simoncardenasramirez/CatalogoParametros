package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheOrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config.RedisCacheProperties;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis.OrganizacionCacheMapper.OrganizacionCacheData;

@Repository
public class RedisOrganizacionRepository extends RedisCatalogoRepository<OrganizacionEntity, OrganizacionCacheData>
        implements CacheOrganizacionRepository {
    public RedisOrganizacionRepository(final StringRedisTemplate redis, final RedisCacheProperties properties) {
        super(redis, properties, "organizacion", "organizacion", OrganizacionCacheData[].class,
                OrganizacionCacheMapper.INSTANCE::toEntity, OrganizacionCacheMapper.INSTANCE::toData);
    }
}
