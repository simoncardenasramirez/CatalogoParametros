package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoTokenEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosEstadoTokenRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheEstadoTokenRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;

@ExtendWith(MockitoExtension.class)
class EstadoTokenCatalogoStrategyTest {
    @Mock private CacheEstadoTokenRepository cache;
    @Mock private BaseDatosEstadoTokenRepository baseDatos;
    private EstadoTokenCatalogoStrategy estrategia;
    private EstadoTokenEntity dato;

    @BeforeEach
    void preparar() {
        estrategia = new EstadoTokenCatalogoStrategy(cache, baseDatos);
        dato = EstadoTokenEntity.create(UUID.randomUUID(), "activo");
    }

    @Test
    void debeEvitarBaseDatosCuandoEstadoTokenEstaEnCache() {
        when(cache.obtenerVersion()).thenReturn("v1");
        when(cache.consultar("v1", "id:" + dato.getId())).thenReturn(Optional.of(List.of(dato)));

        assertSame(dato, estrategia.findById(dato.getId()).orElseThrow());
        verifyNoInteractions(baseDatos);
    }

    @Test
    void debePoblarCacheCuandoEstadoTokenNoEstaEnCache() {
        when(cache.obtenerVersion()).thenReturn("v1");
        when(baseDatos.findById(dato.getId())).thenReturn(Optional.of(dato));

        assertTrue(estrategia.findById(dato.getId()).isPresent());
        verify(cache).guardar("v1", "id:" + dato.getId(), List.of(dato));
    }

    @Test
    void debeRetornarListaVaciaDesdeCacheSinConsultarBaseDatos() {
        when(cache.obtenerVersion()).thenReturn("v1");
        when(cache.consultar("v1", "todos")).thenReturn(Optional.of(List.of()));

        assertTrue(estrategia.findAll().isEmpty());
        verifyNoInteractions(baseDatos);
    }

    @Test
    void debeConsultarBaseDatosCuandoRedisNoEstaDisponible() {
        when(cache.obtenerVersion()).thenThrow(new TechnicalException("Redis no disponible"));
        when(baseDatos.findAll()).thenReturn(List.of(dato));

        assertEquals(List.of(dato), estrategia.findAll());
        verify(cache, never()).guardar(any(), any(), any());
    }
}
