package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config.RedisCacheProperties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RedisOrganizacionRepositoryTest {
    @Mock private StringRedisTemplate redis;
    @Mock private ValueOperations<String, String> valores;
    private RedisOrganizacionRepository repositorio;
    private RedisCacheProperties properties;
    private static final String PREFIJO = "catalogo-parametros:organizacion:v1:";

    @BeforeEach
    void preparar() {
        properties = new RedisCacheProperties();
        repositorio = new RedisOrganizacionRepository(redis, properties);
    }

    @Test
    void debePreservarCamposYTTLCuandoSerializaOrganizacion() {
        when(redis.opsForValue()).thenReturn(valores);
        var dato = OrganizacionEntity.create(UUID.randomUUID(), "Ejemplo", java.time.OffsetDateTime.parse("2026-10-14T09:30:00-05:00"), java.time.OffsetDateTime.parse("2026-10-14T09:30:00-05:00"));
        repositorio.guardar("v1", "todos", List.of(dato));
        var json = ArgumentCaptor.forClass(String.class);
        verify(valores).set(eq(PREFIJO + "v1:todos"), json.capture(), eq(Duration.ofMinutes(5)));
        when(valores.get(PREFIJO + "v1:todos")).thenReturn(json.getValue());
        var recuperado = repositorio.consultar("v1", "todos").orElseThrow().getFirst();
        assertEquals(dato.getId(), recuperado.getId());
        assertEquals(dato.getNombre(), recuperado.getNombre());
        assertEquals((dato.getFechaInicio()).withOffsetSameInstant(java.time.ZoneOffset.UTC), recuperado.getFechaInicio());
        assertEquals((dato.getFechaFinal()).withOffsetSameInstant(java.time.ZoneOffset.UTC), recuperado.getFechaFinal());
    }

    @Test
    void debeDistinguirAusenciaDeListaVaciaCuandoLeeCache() {
        when(redis.opsForValue()).thenReturn(valores);
        when(valores.get(PREFIJO + "v1:todos")).thenReturn(null, "[]");
        assertTrue(repositorio.consultar("v1", "todos").isEmpty());
        assertEquals(List.of(), repositorio.consultar("v1", "todos").orElseThrow());
    }

    @Test
    void debeConservarFechasNulasCuandoConsultaCache() {
        when(redis.opsForValue()).thenReturn(valores);
        var dato = OrganizacionEntity.create(UUID.randomUUID(), "Ejemplo", null, null);
        repositorio.guardar("v1", "todos", List.of(dato));
        var json = ArgumentCaptor.forClass(String.class);
        verify(valores).set(eq(PREFIJO + "v1:todos"), json.capture(), eq(Duration.ofMinutes(5)));
        when(valores.get(PREFIJO + "v1:todos")).thenReturn(json.getValue());
        var recuperado = repositorio.consultar("v1", "todos").orElseThrow().getFirst();
        assertNull(recuperado.getFechaInicio());
        assertNull(recuperado.getFechaFinal());
    }

    @Test
    void debeLanzarExcepcionPropiaCuandoFechaCacheEsInvalida() {
        when(redis.opsForValue()).thenReturn(valores);
        when(valores.get(PREFIJO + "v1:todos")).thenReturn("[{\"fechaInicio\":\"invalida\"}]");
        assertThrows(TechnicalException.class, () -> repositorio.consultar("v1", "todos"));
    }

    @Test
    void debeInicializarVersionAtomicamenteCuandoNoExiste() {
        when(redis.opsForValue()).thenReturn(valores);
        when(valores.get(PREFIJO + "version")).thenReturn(null, "version-concurrente");
        assertEquals("version-concurrente", repositorio.obtenerVersion());
        verify(valores).setIfAbsent(eq(PREFIJO + "version"), anyString());
    }

    @Test
    void debeReutilizarVersionCuandoYaExiste() {
        when(redis.opsForValue()).thenReturn(valores);
        when(valores.get(PREFIJO + "version")).thenReturn("v1");
        assertEquals("v1", repositorio.obtenerVersion());
        verify(valores, never()).setIfAbsent(anyString(), anyString());
    }

    @Test
    void debeCambiarVersionSinBorrarOtrasCachesCuandoInvalida() {
        when(redis.opsForValue()).thenReturn(valores);
        repositorio.invalidar();
        var version = ArgumentCaptor.forClass(String.class);
        verify(valores).set(eq(PREFIJO + "version"), version.capture());
        assertNotNull(UUID.fromString(version.getValue()));
        verify(redis, never()).keys(anyString());
        verify(redis, never()).delete(anyString());
    }

    @Test
    void debeLanzarExcepcionPropiaCuandoRedisFalla() {
        when(redis.opsForValue()).thenReturn(valores);
        when(valores.get(PREFIJO + "version")).thenThrow(new RedisConnectionFailureException("Conexion fallida"));
        var error = assertThrows(TechnicalException.class, repositorio::obtenerVersion);
        assertInstanceOf(RedisConnectionFailureException.class, error.getCause());
    }

    @Test
    void debeLanzarExcepcionPropiaCuandoJsonEsInvalido() {
        when(redis.opsForValue()).thenReturn(valores);
        when(valores.get(PREFIJO + "v1:todos")).thenReturn("no es json");
        assertThrows(TechnicalException.class, () -> repositorio.consultar("v1", "todos"));
    }

    @Test
    void debeEvitarRedisCuandoCacheEstaDeshabilitada() {
        properties.setHabilitada(false);
        repositorio = new RedisOrganizacionRepository(redis, properties);
        assertEquals("deshabilitada", repositorio.obtenerVersion());
        assertTrue(repositorio.consultar("deshabilitada", "todos").isEmpty());
        repositorio.guardar("deshabilitada", "todos", List.of());
        repositorio.invalidar();
        verifyNoInteractions(redis);
    }

    @Test
    void debeRechazarConfiguracionCuandoTTLNoEsPositivo() {
        assertThrows(TechnicalException.class, () -> properties.setTtl(Duration.ZERO));
        assertThrows(TechnicalException.class, () -> properties.setTtl(Duration.ofSeconds(-1)));
        assertThrows(TechnicalException.class, () -> properties.setPrefijo(" "));
    }
}
