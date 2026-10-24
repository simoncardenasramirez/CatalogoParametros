package co.edu.uco.CatalogoParametrosUcoLab.application.features.vigencia;

import java.time.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.vigencia.usecase.GestionarEstadosCatalogoImpl;
import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.*;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.*;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.publisher.CambiosEstadoCatalogoPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.TechnicalException;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import static co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoRegistroVigencia.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class GestionarEstadosCatalogoImplTest {
    private final EstadoCatalogoRepository repository = mock(EstadoCatalogoRepository.class);
    private final CacheParametroRepository cache = mock(CacheParametroRepository.class);
    private final CambiosEstadoCatalogoPublisher publisher = mock(CambiosEstadoCatalogoPublisher.class);
    private final Instant ahora = Instant.parse("2026-10-23T17:00:00Z");
    private final GestionarEstadosCatalogoImpl caso = new GestionarEstadosCatalogoImpl(repository,List.of(cache),
            publisher,new TelemetryService(new SimpleMeterRegistry()),Clock.fixed(ahora,ZoneOffset.UTC));
    private CatalogoVigencia catalogo(Instant fin) {
        var org=UUID.randomUUID();
        return new CatalogoVigencia(List.of(new RegistroVigencia(ORGANIZACION,org,null,null,null,true,null,null),
                new RegistroVigencia(APLICACION,UUID.randomUUID(),org,null,null,true,null,fin)),Map.of(),Map.of());
    }

    @Test void debeInvalidarYNotificarDespuesDePersistirCuandoHayVencidos() {
        var datos=catalogo(ahora);
        when(repository.consultar()).thenReturn(datos);
        caso.sincronizar();
        var orden=inOrder(repository,cache,publisher);
        orden.verify(repository).desactivar(eq(List.of(datos.registros().get(1))),any(),any());
        orden.verify(cache).invalidar();
        orden.verify(publisher).publicar(List.of(datos.registros().get(1)));
    }
    @Test void debeEvitarEscriturasCuandoNoHayCambios() {
        when(repository.consultar()).thenReturn(catalogo(ahora.plusSeconds(1)));
        caso.sincronizar();
        verify(repository,never()).desactivar(anyList(),any(),any());
        verifyNoInteractions(cache,publisher);
    }
    @Test void debeConservarCacheYEventosCuandoFallaLaTransaccion() {
        when(repository.consultar()).thenReturn(catalogo(ahora));
        doThrow(TechnicalException.build("Fallo")).when(repository).desactivar(anyList(),any(),any());
        assertThrows(TechnicalException.class,caso::sincronizar);
        verifyNoInteractions(cache,publisher);
    }
    @Test void debeNotificarCambioConfirmadoCuandoFallaRedis() {
        when(repository.consultar()).thenReturn(catalogo(ahora));
        doThrow(TechnicalException.build("Redis no disponible")).when(cache).invalidar();
        assertDoesNotThrow(caso::sincronizar);
        verify(publisher).publicar(anyList());
    }
    @Test void debeConsultarPersistenciaCuandoCompruebaDisponibilidadSinEsperarScheduler() {
        var datos=catalogo(ahora);
        when(repository.consultar()).thenReturn(datos);
        assertFalse(caso.disponible(APLICACION,datos.registros().get(1).id()));
        verify(repository).consultar();
        verifyNoInteractions(cache,publisher);
    }
    @Test void debeEvitarReconciliacionCompletaCuandoEditaRegistroSinDesactivarlo() {
        var datos=catalogo(ahora.plusSeconds(60));
        when(repository.consultar()).thenReturn(datos);
        var registro=datos.registros().get(1);
        assertEquals("guardado",caso.guardar(registro,false,()->"guardado"));
        verify(repository,times(1)).consultar();
        verify(repository,never()).normalizarFechasUtc();
        verify(repository,never()).desactivar(anyList(),any(),any());
        verifyNoInteractions(cache,publisher);
    }
    @Test void debePropagarInmediatamenteCuandoDesactivaUnPadre() {
        var datos=catalogo(ahora.plusSeconds(60));
        var anterior=datos.registros().get(1);
        var inactivo=new RegistroVigencia(APLICACION,anterior.id(),anterior.idPadre(),null,null,false,null,anterior.fin());
        var despues=new CatalogoVigencia(List.of(datos.registros().get(0),inactivo),Map.of(),Map.of());
        when(repository.consultar()).thenReturn(datos,despues);
        assertEquals("guardado",caso.guardar(inactivo,false,()->"guardado"));
        verify(repository).normalizarFechasUtc();
        verify(repository,times(2)).consultar();
    }
    @Test void debeConservarReactivacionIndividualSinRecorrerCatalogoCuandoReactivaUnPadre() {
        var datos=catalogo(ahora.plusSeconds(60));
        var activo=datos.registros().get(1);
        var anterior=new RegistroVigencia(APLICACION,activo.id(),activo.idPadre(),null,null,false,null,activo.fin());
        when(repository.consultar()).thenReturn(new CatalogoVigencia(List.of(datos.registros().get(0),anterior),Map.of(),Map.of()));
        caso.guardar(activo,false,()->"guardado");
        verify(repository,never()).normalizarFechasUtc();
        verify(repository,times(1)).consultar();
    }
}
