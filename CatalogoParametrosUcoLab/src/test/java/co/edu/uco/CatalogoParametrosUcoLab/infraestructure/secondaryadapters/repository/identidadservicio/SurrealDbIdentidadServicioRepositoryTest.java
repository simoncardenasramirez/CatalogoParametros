package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.identidadservicio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.IdentidadServicioEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

class SurrealDbIdentidadServicioRepositoryTest {
    private static final UUID ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID APLICACION = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID AMBIENTE = UUID.fromString("33333333-3333-3333-3333-333333333333");

    private final SurrealDbClient client = mock(SurrealDbClient.class);
    private final SurrealDbIdentidadServicioRepository repository = new SurrealDbIdentidadServicioRepository(client);
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void debeGuardarYEliminarLaIdentidadEnTransacciones() {
        final var entity = IdentidadServicioEntity.create(ID, APLICACION, AMBIENTE, "client'service");

        assertSame(entity, repository.save(entity));
        repository.deleteById(ID);

        final var queries = ArgumentCaptor.forClass(String.class);
        verify(client, org.mockito.Mockito.times(2)).execute(queries.capture());
        assertTrue(queries.getAllValues().get(0).contains("idAplicacion: '" + APLICACION + "'"));
        assertTrue(queries.getAllValues().get(0).contains("clientId: 'client\\'service'"));
        assertTrue(queries.getAllValues().get(1).contains("DELETE type::record('identidades_servicio', '" + ID));
    }

    @Test
    void debeConsultarIdentidadesPorAplicacionYPorClientId() {
        when(client.execute(anyString())).thenReturn(response(record()));

        assertEquals(1, repository.findByIdAplicacion(APLICACION).size());
        assertEquals(ID, repository.findByClientId("client-id").orElseThrow().getId());
        assertEquals(ID, repository.findByIdAplicacionAndClientId(APLICACION, "client-id").orElseThrow().getId());
    }

    @Test
    void debeRetornarAusenciaCuandoLasConsultasNoTienenFilas() {
        when(client.execute(anyString())).thenReturn(mapper.createArrayNode());

        assertTrue(repository.findByClientId("client-id").isEmpty());
        assertTrue(repository.findByIdAplicacionAndClientId(APLICACION, "client-id").isEmpty());
        assertTrue(repository.findByIdAplicacion(APLICACION).isEmpty());
    }

    @Test
    void debeNormalizarIdentificadoresInvalidos() {
        when(client.execute(anyString())).thenReturn(response(record().put("id", "identidades_servicio:invalid")
                .put("idAplicacion", "invalid").put("idAmbiente", "invalid")));

        final var entity = repository.findByClientId("client-id").orElseThrow();

        assertEquals(new UUID(0, 0), entity.getId());
        assertEquals(new UUID(0, 0), entity.getIdAplicacion());
        assertEquals(new UUID(0, 0), entity.getIdAmbiente());
    }

    private ObjectNode record() {
        return mapper.createObjectNode()
                .put("id", "identidades_servicio:`" + ID + "`")
                .put("idAplicacion", APLICACION.toString())
                .put("idAmbiente", AMBIENTE.toString())
                .put("clientId", "client-id");
    }

    private tools.jackson.databind.JsonNode response(final ObjectNode... records) {
        final var response = mapper.createArrayNode();
        response.addObject().putArray("result");
        final var rows = response.addObject().putArray("result");
        for (final var record : records) rows.add(record);
        return response;
    }
}
