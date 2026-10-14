package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AmbienteCatalogoStrategyTest {
    @Mock private CacheAmbienteRepository cache;
    @Mock private BaseDatosAmbienteRepository baseDatos;
    private AmbienteCatalogoStrategy estrategia;
    private AmbienteEntity dato;

    @BeforeEach
    void preparar() {
        estrategia = new AmbienteCatalogoStrategy(cache, baseDatos);
        dato = AmbienteEntity.create(UUID.randomUUID(), "Ejemplo");
    }

    @Test
    void debeEvitarBaseDatosCuandoAmbienteEstaEnCache() {
        when(cache.obtenerVersion()).thenReturn("v1");
        when(cache.consultar("v1", "id:" + dato.getId())).thenReturn(Optional.of(List.of(dato)));
        assertSame(dato, estrategia.findById(dato.getId()).orElseThrow());
        verifyNoInteractions(baseDatos);
    }

    @Test
    void debePoblarCacheCuandoAmbienteNoEstaEnCache() {
        when(cache.obtenerVersion()).thenReturn("v1");
        when(baseDatos.findById(dato.getId())).thenReturn(Optional.of(dato));
        assertTrue(estrategia.findById(dato.getId()).isPresent());
        verify(cache).guardar("v1", "id:" + dato.getId(), List.of(dato));
    }

    @Test
    void debeRetornarVacioCuandoAmbienteNoExiste() {
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
    void debePropagarErrorDeBaseDatosCuandoCacheTambienFalla() {
        when(cache.obtenerVersion()).thenThrow(new TechnicalException("Cache no disponible"));
        var error = new TechnicalException("Base de datos no disponible");
        when(baseDatos.findAll()).thenThrow(error);

        assertSame(error, assertThrows(TechnicalException.class, estrategia::findAll));

        verify(baseDatos).findAll();
        verify(cache, never()).guardar(any(), any(), any());
    }

    @Test
    void debeInvalidarDespuesDeConfirmarCuandoCreaAmbiente() {
        when(baseDatos.save(dato)).thenReturn(dato);
        assertSame(dato, estrategia.save(dato));
        var orden = inOrder(baseDatos, cache);
        orden.verify(baseDatos).save(dato);
        orden.verify(cache).invalidar();
    }

    @Test
    void debeInvalidarCuandoActualizaAmbienteOEstado() {
        when(baseDatos.update(dato)).thenReturn(dato);
        assertSame(dato, estrategia.update(dato));
        verify(cache).invalidar();
    }

    @Test
    void debeInvalidarCuandoEliminaAmbiente() {
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
    @Test
    void debeSepararClavesCuandoConsultaFindAllPaginado() {
        when(cache.obtenerVersion()).thenReturn("v1");
        when(baseDatos.findAllPaginado(2, 10)).thenReturn(List.of(dato));
        assertEquals(List.of(dato), estrategia.findAllPaginado(2, 10));
        verify(cache).guardar("v1", "pagina:2:10", List.of(dato));
    }
}
