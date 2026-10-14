package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosMetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheMetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetadatoCatalogoStrategyTest {
    @Mock private CacheMetadatoRepository cache;
    @Mock private BaseDatosMetadatoRepository baseDatos;
    private MetadatoCatalogoStrategy estrategia;
    private MetadatoEntity dato;

    @BeforeEach
    void preparar() {
        estrategia = new MetadatoCatalogoStrategy(cache, baseDatos);
        dato = MetadatoEntity.create(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), tools.jackson.databind.json.JsonMapper.builder().build().readTree("{\"lista\":[1,true,null],\"texto\":\"valor\"}"));
    }

    @Test
    void debeEvitarBaseDatosCuandoMetadatoEstaEnCache() {
        when(cache.obtenerVersion()).thenReturn("v1");
        when(cache.consultar("v1", "id:" + dato.getId())).thenReturn(Optional.of(List.of(dato)));
        assertSame(dato, estrategia.findById(dato.getId()).orElseThrow());
        verifyNoInteractions(baseDatos);
    }

    @Test
    void debePoblarCacheCuandoMetadatoNoEstaEnCache() {
        when(cache.obtenerVersion()).thenReturn("v1");
        when(baseDatos.findById(dato.getId())).thenReturn(Optional.of(dato));
        assertTrue(estrategia.findById(dato.getId()).isPresent());
        verify(cache).guardar("v1", "id:" + dato.getId(), List.of(dato));
    }

    @Test
    void debeRetornarVacioCuandoMetadatoNoExiste() {
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
    void debeInvalidarDespuesDeConfirmarCuandoCreaMetadato() {
        when(baseDatos.save(dato)).thenReturn(dato);
        assertSame(dato, estrategia.save(dato));
        var orden = inOrder(baseDatos, cache);
        orden.verify(baseDatos).save(dato);
        orden.verify(cache).invalidar();
    }

    @Test
    void debeInvalidarCuandoActualizaMetadatoOEstado() {
        when(baseDatos.update(dato)).thenReturn(dato);
        assertSame(dato, estrategia.update(dato));
        verify(cache).invalidar();
    }

    @Test
    void debeInvalidarCuandoEliminaMetadato() {
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
    void debeSepararClavesCuandoConsultaFindByIdParametro() {
        when(cache.obtenerVersion()).thenReturn("v1");
        when(baseDatos.findByIdParametro(dato.getIdParametro())).thenReturn(List.of(dato));
        assertEquals(List.of(dato), estrategia.findByIdParametro(dato.getIdParametro()));
        verify(cache).guardar("v1", "parametro:" + dato.getIdParametro(), List.of(dato));
    }
}
