package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.metadatoambiente;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.UUID;
import org.junit.jupiter.api.Test;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

class SurrealDbMetadatoAmbienteRepositoryTest {
    private static final UUID ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID PARAMETRO = UUID.fromString("22222222-2222-2222-2222-222222222222");
    private static final UUID AMBIENTE = UUID.fromString("33333333-3333-3333-3333-333333333333");
    private static final UUID ESTADO = UUID.fromString("44444444-4444-4444-4444-444444444444");
    private final SurrealDbClient client = mock(SurrealDbClient.class);
    private final SurrealDbMetadatoAmbienteRepository repository = new SurrealDbMetadatoAmbienteRepository(client);
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void debeEjecutarCrudYConsultas() {
        final var entity = MetadatoAmbienteEntity.create(ID, PARAMETRO, AMBIENTE, ESTADO);
        when(client.execute(anyString())).thenReturn(response(record(false)));
        assertSame(entity, repository.save(entity));
        assertSame(entity, repository.update(entity));
        repository.deleteById(ID);
        assertEquals(PARAMETRO, repository.findById(ID).orElseThrow().getIdParametro());
        assertEquals(1, repository.findAll().size());
        assertEquals(1, repository.findAllPaginado(2, 5).size());
        assertTrue(repository.existsByIdAmbiente(AMBIENTE));
        assertTrue(repository.existsByIdEstadoMetadatoAmbiente(ESTADO));
        verify(client, atLeastOnce()).execute(contains("BEGIN TRANSACTION;"));
        verify(client).execute("SELECT * FROM metadatos_ambiente LIMIT 5 START 5;");
    }

    @Test
    void debeManejarRespuestasVaciasEIdentificadoresInvalidos() {
        when(client.execute(anyString())).thenReturn(mapper.createArrayNode());
        assertTrue(repository.findById(ID).isEmpty());
        assertTrue(repository.findAll().isEmpty());
        assertFalse(repository.existsByIdAmbiente(AMBIENTE));
        when(client.execute(anyString())).thenReturn(response(record(true)));
        final var entity = repository.findById(ID).orElseThrow();
        assertEquals(new UUID(0, 0), entity.getId());
        assertEquals(new UUID(0, 0), entity.getIdParametro());
    }

    private ObjectNode record(final boolean invalid) {
        final var value = invalid ? "invalid" : PARAMETRO.toString();
        return mapper.createObjectNode()
                .put("id", invalid ? "" : "metadatos_ambiente:" + ID)
                .put("idParametro", value)
                .put("idAmbiente", invalid ? "invalid" : AMBIENTE.toString())
                .put("idEstadoMetadatoAmbiente", invalid ? "invalid" : ESTADO.toString());
    }

    private tools.jackson.databind.JsonNode response(final ObjectNode record) {
        final var response = mapper.createArrayNode();
        response.addObject().putArray("result");
        response.addObject().putArray("result").add(record);
        return response;
    }
}
