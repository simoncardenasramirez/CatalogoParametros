package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.vigencia;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.*;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.*;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;
import static co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoRegistroVigencia.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SurrealDbEstadoCatalogoRepositoryTest {
    private final SurrealDbClient client=mock(SurrealDbClient.class);
    private final SurrealDbEstadoCatalogoRepository repository = new SurrealDbEstadoCatalogoRepository(client);

    @Test void debeConfirmarTodosLosCambiosEnUnaTransaccionCuandoDesactivaUnaCadena() {
        var app=UUID.randomUUID();
        var modulo=UUID.randomUUID();
        var asociacion=UUID.randomUUID();
        var estadoAnterior=UUID.randomUUID();
        var nuevoEstado=UUID.randomUUID();
        var registros=List.of(
                new RegistroVigencia(MODULO,modulo,app,null,null,true,null,Instant.parse("2026-10-23T17:00:00Z")),
                new RegistroVigencia(METADATO_AMBIENTE,asociacion,UUID.randomUUID(),UUID.randomUUID(),
                        estadoAnterior,true,null,null));
        repository.desactivar(registros,nuevoEstado,UUID.randomUUID());
        var captura=ArgumentCaptor.forClass(String.class);
        verify(client,times(1)).execute(captura.capture());
        var sql=captura.getValue();
        assertTrue(sql.startsWith("BEGIN TRANSACTION;"));
        assertTrue(sql.endsWith("COMMIT TRANSACTION;"));
        assertTrue(sql.contains(modulo.toString()));
        assertTrue(sql.contains(asociacion.toString()));
        assertTrue(sql.contains("WHERE activo = true AND idAplicacion = '"+app+"'"));
        assertTrue(sql.contains("WHERE idEstadoMetadatoAmbiente = '"+estadoAnterior+"'"));
        assertTrue(sql.contains("<datetime>fechaFinal = d'2026-10-23T17:00:00Z'"));
        assertFalse(sql.contains("CONTENT"),"No sobrescribir nombres, relaciones ni fechas.");
        assertFalse(sql.contains("UPDATE ambientes"),"El ambiente compartido no tiene estado propio.");
    }
    @Test void debeEvitarAccesoCuandoPlanEstaVacio() {
        repository.desactivar(List.of(),UUID.randomUUID(),UUID.randomUUID());
        verifyNoInteractions(client);
    }
    @Test void debeConsultarProyeccionesEnUnaPeticionCuandoEvaluaDisponibilidad() {
        var org=UUID.randomUUID(); var app=UUID.randomUUID();
        var respuesta=new tools.jackson.databind.ObjectMapper().readTree(
                "[{\"result\":[{\"id\":\"organizaciones:"+org+"\"}]},"
                + "{\"result\":[{\"id\":\"aplicaciones:"+app+"\",\"idOrganizacion\":\""+org
                +"\",\"activa\":true,\"fechaFinal\":\"2026-10-23T12:00:00-05:00\"}]},"
                + "{\"result\":[]},{\"result\":[]},{\"result\":[]},{\"result\":[]},"
                + "{\"result\":[]},{\"result\":[]},{\"result\":[]}]");
        when(client.execute(anyString())).thenReturn(respuesta);
        var catalogo=repository.consultar();
        var captura=ArgumentCaptor.forClass(String.class);
        verify(client,times(1)).execute(captura.capture());
        assertFalse(captura.getValue().contains("SELECT *"));
        assertFalse(captura.getValue().contains("tokenHash"));
        assertEquals(2,catalogo.registros().size());
        var aplicacion=catalogo.registros().stream().filter(r->r.tipo()==APLICACION).findFirst().orElseThrow();
        assertTrue(aplicacion.activo());
        assertEquals(org,aplicacion.idPadre());
        assertEquals(Instant.parse("2026-10-23T17:00:00Z"),aplicacion.fin());
    }
    @Test void debeNormalizarFechasHistoricasSinAgregarEstadoCuandoLeeUnDesfase() {
        var id=UUID.randomUUID();
        var respuesta = new tools.jackson.databind.ObjectMapper().readTree(
                "[{\"result\":[{\"id\":\"organizaciones:" + id
                + "\",\"fechaInicio\":\"2026-01-02T10:30:00-05:00\"}]},"
                + "{\"result\":[]},{\"result\":[]},{\"result\":[]},{\"result\":[]}]");
        when(client.execute(anyString())).thenReturn(respuesta);
        assertEquals(1,repository.normalizarFechasUtc());
        var captura=ArgumentCaptor.forClass(String.class);
        verify(client,times(2)).execute(captura.capture());
        var sql=captura.getAllValues().get(1);
        assertTrue(sql.contains("SET fechaInicio = '2026-01-02T15:30:00Z'"));
        assertTrue(sql.contains("WHERE fechaInicio = '2026-01-02T10:30:00-05:00'"));
        assertFalse(sql.contains("activo"));
        assertFalse(sql.contains("estado"));
    }
    @Test void debeRespetarEstadoAnteriorCuandoTokenPuedeHaberSidoRevocadoConcurrentemente() {
        var estadoActivo=UUID.randomUUID();
        var estadoInactivo=UUID.randomUUID();
        repository.desactivar(List.of(new RegistroVigencia(TOKEN,UUID.randomUUID(),UUID.randomUUID(),
                UUID.randomUUID(),estadoActivo,true,null,Instant.EPOCH)),UUID.randomUUID(),estadoInactivo);
        var captura=ArgumentCaptor.forClass(String.class);
        verify(client).execute(captura.capture());
        assertTrue(captura.getValue().contains("SET estadoToken = '"+estadoInactivo+"' WHERE estadoToken = '"+estadoActivo+"'"));
    }
}
