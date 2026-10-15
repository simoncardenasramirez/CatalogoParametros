package co.edu.uco.CatalogoParametrosUcoLab.application.catalogo.estrategia;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CacheParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParametroCatalogoStrategyTest {
    @Mock private CacheParametroRepository cache;
    @Mock private BaseDatosParametroRepository baseDatos;
    private ParametroCatalogoStrategy estrategia;
    private ParametroEntity parametro;

    @BeforeEach
    void preparar() {
        estrategia = new ParametroCatalogoStrategy(cache, baseDatos);
        parametro = ParametroEntity.create(UUID.randomUUID(), "Limite", UUID.randomUUID(), UUID.randomUUID(), true);
    }

    @Test
    void debeEvitarBaseDatosCuandoParametroEstaEnCache() {
        when(cache.obtenerVersion()).thenReturn("v1");
        when(cache.consultar("v1", "id:" + parametro.getId())).thenReturn(Optional.of(List.of(parametro)));
        assertSame(parametro, estrategia.findById(parametro.getId()).orElseThrow());
        verifyNoInteractions(baseDatos);
    }

    @Test
    void debePoblarCacheCuandoParametroNoEstaEnCache() {
        when(cache.obtenerVersion()).thenReturn("v1");
        when(baseDatos.findById(parametro.getId())).thenReturn(Optional.of(parametro));
        assertTrue(estrategia.findById(parametro.getId()).isPresent());
        verify(cache).guardar("v1", "id:" + parametro.getId(), List.of(parametro));
    }

    @Test
    void debeRetornarVacioCuandoParametroNoExiste() {
        when(cache.obtenerVersion()).thenReturn("v1");
        when(baseDatos.findById(parametro.getId())).thenReturn(Optional.empty());
        assertTrue(estrategia.findById(parametro.getId()).isEmpty());
        verify(cache).guardar("v1", "id:" + parametro.getId(), List.of());
    }

    @Test
    void debeRetornarListaVaciaSinConsultarBaseDatosCuandoEstaCacheada() {
        when(cache.obtenerVersion()).thenReturn("v1");
        when(cache.consultar("v1", "todos")).thenReturn(Optional.of(List.of()));
        assertTrue(estrategia.findAll().isEmpty());
        verifyNoInteractions(baseDatos);
    }

    @Test
    void debeSepararPaginasYFuncionalidadesCuandoConsultaListados() {
        when(cache.obtenerVersion()).thenReturn("v1");
        when(baseDatos.findAllPaginado(2, 10)).thenReturn(List.of(parametro));
        when(baseDatos.findByIdFuncionalidad(parametro.getIdFuncionalidad())).thenReturn(List.of(parametro));
        assertEquals(List.of(parametro), estrategia.findAllPaginado(2, 10));
        assertEquals(List.of(parametro), estrategia.findByIdFuncionalidad(parametro.getIdFuncionalidad()));
        verify(cache).guardar("v1", "pagina:2:10", List.of(parametro));
        verify(cache).guardar("v1", "funcionalidad:" + parametro.getIdFuncionalidad(), List.of(parametro));
    }

    @Test
    void debeSepararCachePorAplicacionYAmbiente() {
        var aplicacionId = UUID.randomUUID();
        var ambienteId = UUID.randomUUID();
        when(cache.obtenerVersion()).thenReturn("v1");
        when(baseDatos.findByAplicacionAndAmbiente(aplicacionId, ambienteId)).thenReturn(List.of(parametro));

        assertEquals(List.of(parametro), estrategia.findByAplicacionAndAmbiente(aplicacionId, ambienteId));
        verify(cache).guardar("v1", "aplicacion:" + aplicacionId + ":ambiente:" + ambienteId,
                List.of(parametro));
    }

    @Test
    void debeConsultarBaseDatosCuandoRedisNoEstaDisponible() {
        when(cache.obtenerVersion()).thenThrow(new TechnicalException("Redis no disponible"));
        when(baseDatos.findAll()).thenReturn(List.of(parametro));
        assertEquals(List.of(parametro), estrategia.findAll());
        verify(cache, never()).guardar(any(), any(), any());
    }

    @Test
    void debeConsultarBaseDatosCuandoLecturaCacheFalla() {
        when(cache.obtenerVersion()).thenReturn("v1");
        when(cache.consultar("v1", "todos")).thenThrow(new TechnicalException("Redis no disponible"));
        when(baseDatos.findAll()).thenReturn(List.of(parametro));
        assertEquals(List.of(parametro), estrategia.findAll());
    }

    @Test
    void debeRetornarDatosCuandoFallaEscrituraCache() {
        when(cache.obtenerVersion()).thenReturn("v1");
        when(baseDatos.findAll()).thenReturn(List.of(parametro));
        doThrow(new TechnicalException("Redis no disponible")).when(cache).guardar("v1", "todos", List.of(parametro));
        assertEquals(List.of(parametro), estrategia.findAll());
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
    void debeInvalidarDespuesDeConfirmarCuandoCreaParametro() {
        when(baseDatos.save(parametro)).thenReturn(parametro);
        assertSame(parametro, estrategia.save(parametro));
        var orden = inOrder(baseDatos, cache);
        orden.verify(baseDatos).save(parametro);
        orden.verify(cache).invalidar();
    }

    @Test
    void debeInvalidarCuandoActualizaParametroOEstado() {
        parametro.setActivo(false);
        when(baseDatos.update(parametro)).thenReturn(parametro);
        assertFalse(estrategia.update(parametro).isActivo());
        verify(cache).invalidar();
    }

    @Test
    void debeInvalidarCuandoEliminaParametro() {
        estrategia.deleteById(parametro.getId());
        var orden = inOrder(baseDatos, cache);
        orden.verify(baseDatos).deleteById(parametro.getId());
        orden.verify(cache).invalidar();
    }

    @Test
    void debeConservarCacheCuandoMutacionFalla() {
        doThrow(new TechnicalException("Transaccion fallida")).when(baseDatos).deleteById(parametro.getId());
        assertThrows(TechnicalException.class, () -> estrategia.deleteById(parametro.getId()));
        verifyNoInteractions(cache);
    }

    @Test
    void debeRetornarMutacionConfirmadaCuandoInvalidacionFalla() {
        when(baseDatos.save(parametro)).thenReturn(parametro);
        doThrow(new TechnicalException("Redis no disponible")).when(cache).invalidar();
        assertSame(parametro, estrategia.save(parametro));
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
            estrategia.deleteById(parametro.getId());
            return List.of(parametro);
        });
        estrategia.findAll();
        var orden = inOrder(cache);
        orden.verify(cache).obtenerVersion();
        orden.verify(cache).consultar("v1", "todos");
        orden.verify(cache).invalidar();
        orden.verify(cache).guardar("v1", "todos", List.of(parametro));
    }
}
