package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosTipoParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheTipoParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoParametroCatalogoStrategyTest {
    @Mock private CacheTipoParametroRepository cache;
    @Mock private BaseDatosTipoParametroRepository baseDatos;
    private TipoParametroCatalogoStrategy estrategia;
    private TipoParametroEntity dato;

    @BeforeEach
    void preparar() {
        estrategia = new TipoParametroCatalogoStrategy(cache, baseDatos);
        dato = TipoParametroEntity.create(UUID.randomUUID(), "Ejemplo");
    }

    @Test
    void debeEvitarBaseDatosCuandoTipoParametroEstaEnCache() {
        when(cache.obtenerVersion()).thenReturn("v1");
        when(cache.consultar("v1", "id:" + dato.getId())).thenReturn(Optional.of(List.of(dato)));
        assertSame(dato, estrategia.findById(dato.getId()).orElseThrow());
        verifyNoInteractions(baseDatos);
    }

    @Test
    void debePoblarCacheCuandoTipoParametroNoEstaEnCache() {
        when(cache.obtenerVersion()).thenReturn("v1");
        when(baseDatos.findById(dato.getId())).thenReturn(Optional.of(dato));
        assertTrue(estrategia.findById(dato.getId()).isPresent());
        verify(cache).guardar("v1", "id:" + dato.getId(), List.of(dato));
    }

    @Test
    void debeRetornarVacioCuandoTipoParametroNoExiste() {
        when(cache.obtenerVersion()).thenReturn("v1");
        when(baseDatos.findById(dato.getId())).thenReturn(Optional.empty());
        assertTrue(estrategia.findById(dato.getId()).isEmpty());
        verify(cache).guardar("v1", "id:" + dato.getId(), List.of());
    }

    @Test
    void debeRetornarListaVaciaSinConsultarBaseDatosCuandoEstaCacheada() {
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

    @Test
    void debeConsultarBaseDatosCuandoLecturaCacheFalla() {
        when(cache.obtenerVersion()).thenReturn("v1");
        when(cache.consultar("v1", "todos")).thenThrow(new TechnicalException("Redis no disponible"));
        when(baseDatos.findAll()).thenReturn(List.of(dato));
        assertEquals(List.of(dato), estrategia.findAll());
    }

    @Test
    void debeRetornarDatosCuandoFallaEscrituraCache() {
        when(cache.obtenerVersion()).thenReturn("v1");
        when(baseDatos.findAll()).thenReturn(List.of(dato));
        doThrow(new TechnicalException("Redis no disponible")).when(cache).guardar("v1", "todos", List.of(dato));
        assertEquals(List.of(dato), estrategia.findAll());
    }

    @Test
    void debePropagarErrorCuandoBaseDatosFalla() {
        when(cache.obtenerVersion()).thenReturn("v1");
        var error = new TechnicalException("SurrealDB no disponible");
        when(baseDatos.findAll()).thenThrow(error);
        assertSame(error, assertThrows(TechnicalException.class, estrategia::findAll));
        verify(cache, never()).guardar(any(), any(), any());
    }

    @Test
    void debeInvalidarDespuesDeConfirmarCuandoCreaTipoParametro() {
        when(baseDatos.save(dato)).thenReturn(dato);
        assertSame(dato, estrategia.save(dato));
        var orden = inOrder(baseDatos, cache);
        orden.verify(baseDatos).save(dato);
        orden.verify(cache).invalidar();
    }

    @Test
    void debeInvalidarCuandoActualizaTipoParametroOEstado() {
        when(baseDatos.update(dato)).thenReturn(dato);
        assertSame(dato, estrategia.update(dato));
        verify(cache).invalidar();
    }

    @Test
    void debeInvalidarCuandoEliminaTipoParametro() {
        estrategia.deleteById(dato.getId());
        var orden = inOrder(baseDatos, cache);
        orden.verify(baseDatos).deleteById(dato.getId());
        orden.verify(cache).invalidar();
    }

    @Test
    void debeConservarCacheCuandoMutacionFalla() {
        doThrow(new TechnicalException("Transaccion fallida")).when(baseDatos).deleteById(dato.getId());
        assertThrows(TechnicalException.class, () -> estrategia.deleteById(dato.getId()));
        verifyNoInteractions(cache);
    }

    @Test
    void debeRetornarMutacionConfirmadaCuandoInvalidacionFalla() {
        when(baseDatos.save(dato)).thenReturn(dato);
        doThrow(new TechnicalException("Redis no disponible")).when(cache).invalidar();
        assertSame(dato, estrategia.save(dato));
    }

    @Test
    void debeValidarEnBaseDatosCuandoConsultaUnicidad() {
        when(baseDatos.existsByNombre("Limite")).thenReturn(true);
        assertTrue(estrategia.existsByNombre("Limite"));
        verifyNoInteractions(cache);
    }

    @Test
    void debeGuardarVersionOriginalCuandoHayInvalidacionConcurrente() {
        when(cache.obtenerVersion()).thenReturn("v1");
        when(baseDatos.findAll()).thenAnswer(invocacion -> {
            estrategia.deleteById(dato.getId());
            return List.of(dato);
        });
        estrategia.findAll();
        var orden = inOrder(cache);
        orden.verify(cache).obtenerVersion();
        orden.verify(cache).consultar("v1", "todos");
        orden.verify(cache).invalidar();
        orden.verify(cache).guardar("v1", "todos", List.of(dato));
    }
}
