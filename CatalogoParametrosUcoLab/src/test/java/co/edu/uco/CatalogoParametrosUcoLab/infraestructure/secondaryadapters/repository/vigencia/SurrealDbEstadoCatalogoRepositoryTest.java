package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.vigencia;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.*;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;

class SurrealDbEstadoCatalogoRepositoryTest {
    private final SurrealDbClient client = mock(SurrealDbClient.class);
    private final SurrealDbEstadoCatalogoRepository repository = new SurrealDbEstadoCatalogoRepository(client);

    @Test void debeConfirmarCambiosDeVigenciaEnUnaTransaccion() {
        var app = UUID.randomUUID();
        var modulo = UUID.randomUUID();
        var estado = UUID.randomUUID();
        var registros = List.of(new RegistroVigencia(TipoRegistroVigencia.MODULO, modulo, app, null, null,
                true, null, Instant.parse("2026-10-23T17:00:00Z")),
                new RegistroVigencia(TipoRegistroVigencia.METADATO_AMBIENTE, UUID.randomUUID(), UUID.randomUUID(),
                        UUID.randomUUID(), estado, true, null, null));
        repository.desactivar(registros, UUID.randomUUID());
        var captura = ArgumentCaptor.forClass(String.class);
        verify(client).execute(captura.capture());
        var sql = captura.getValue();
        assertTrue(sql.startsWith("BEGIN TRANSACTION;"));
        assertTrue(sql.endsWith("COMMIT TRANSACTION;"));
        assertTrue(sql.contains(modulo.toString()));
        assertFalse(sql.contains("tokens"));
        assertFalse(sql.contains("estadoToken"));
    }

    @Test void noConsultaCuandoNoHayCambios() {
        repository.desactivar(List.of(), UUID.randomUUID());
        verifyNoInteractions(client);
    }

    @Test void debeConsultarSoloProyeccionesDeVigencia() {
        var org = UUID.randomUUID();
        var respuesta = new tools.jackson.databind.ObjectMapper().readTree(
                "[{\"result\":[{\"id\":\"organizaciones:" + org + "\"}]},"
                + "{\"result\":[]},{\"result\":[]},{\"result\":[]},{\"result\":[]},{\"result\":[]}]" );
        when(client.execute(anyString())).thenReturn(respuesta);
        var catalogo = repository.consultar();
        var captura = ArgumentCaptor.forClass(String.class);
        verify(client).execute(captura.capture());
        assertFalse(captura.getValue().contains("SELECT *"));
        assertFalse(captura.getValue().contains("tokenHash"));
        assertEquals(1, catalogo.registros().size());
    }
}
