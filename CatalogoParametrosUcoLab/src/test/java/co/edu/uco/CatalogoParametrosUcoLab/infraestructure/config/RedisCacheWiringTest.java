package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;

import co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia.ParametroCatalogoStrategy;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametro.primaryports.interactor.impl.ConsultarParametroInteractorImpl;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.parametro.SurrealDbParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis.RedisParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;
import tools.jackson.databind.json.JsonMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RedisCacheWiringTest {
    @Test
    void debeInyectarEstrategiaSinAmbiguedadCuandoRegistraAdaptadoresYConsumidor() {
        var redis = mock(StringRedisTemplate.class);
        var surreal = mock(SurrealDbClient.class);
        var properties = new RedisCacheProperties();
        properties.setHabilitada(false);
        when(surreal.execute("SELECT * FROM parametros;"))
                .thenReturn(JsonMapper.builder().build().readTree("[{\"result\": []}]"));

        try (var contexto = new AnnotationConfigApplicationContext()) {
            contexto.registerBean(StringRedisTemplate.class, () -> redis);
            contexto.registerBean(SurrealDbClient.class, () -> surreal);
            contexto.registerBean(RedisCacheProperties.class, () -> properties);
            contexto.register(RedisParametroRepository.class, SurrealDbParametroRepository.class,
                    ParametroCatalogoStrategy.class, ConsultarParametroInteractorImpl.class);
            contexto.refresh();

            assertInstanceOf(ParametroCatalogoStrategy.class, contexto.getBean(ParametroRepository.class));
            assertInstanceOf(SurrealDbParametroRepository.class, contexto.getBean(BaseDatosParametroRepository.class));
            assertInstanceOf(RedisParametroRepository.class, contexto.getBean(CacheParametroRepository.class));
            assertTrue(contexto.getBean(ConsultarParametroInteractorImpl.class).execute().isEmpty());
            verify(redis, never()).opsForValue();
        }
    }
}
