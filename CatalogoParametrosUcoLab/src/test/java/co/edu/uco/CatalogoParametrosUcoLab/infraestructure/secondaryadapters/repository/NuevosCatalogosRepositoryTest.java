package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

class NuevosCatalogosRepositoryTest {
    private static final UUID ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private final ObjectMapper mapper = new ObjectMapper();

    static Stream<String[]> repositories() {
        return Stream.of(new String[] {"ambiente", "Ambiente", "ambientes"},
                new String[] {"estadoambiente", "EstadoAmbiente", "estados_ambiente"},
                new String[] {"estadometadatoambiente", "EstadoMetadatoAmbiente",
                        "estados_metadato_ambiente"});
    }

    @ParameterizedTest
    @MethodSource("repositories")
    void debeEjecutarCrudDeCatalogosNombrados(final String feature, final String name,
            final String table) throws Exception {
        final var client = mock(SurrealDbClient.class);
        final var repositoryClass = Class.forName(
                "co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository."
                        + feature + ".SurrealDb" + name + "Repository");
        final var repository = repositoryClass.getConstructor(SurrealDbClient.class).newInstance(client);
        final var entityClass = Class.forName(
                "co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity."
                        + name + "Entity");
        final var entity = entityClass.getMethod("create", UUID.class, String.class)
                .invoke(null, ID, "O'Reilly\\UCO");

        when(client.execute(anyString())).thenReturn(response(record(table, false)));
        assertSame(entity, repositoryClass.getMethod("save", entityClass).invoke(repository, entity));
        assertSame(entity, repositoryClass.getMethod("update", entityClass).invoke(repository, entity));
        repositoryClass.getMethod("deleteById", UUID.class).invoke(repository, ID);
        assertTrue((boolean) repositoryClass.getMethod("existsByNombre", String.class)
                .invoke(repository, "O'Reilly"));
        assertTrue(((java.util.Optional<?>) repositoryClass.getMethod("findById", UUID.class)
                .invoke(repository, ID)).isPresent());
        assertEquals(1, ((List<?>) repositoryClass.getMethod("findAll").invoke(repository)).size());
        assertEquals(1, ((List<?>) repositoryClass.getMethod("findAllPaginado", int.class, int.class)
                .invoke(repository, 3, 5)).size());

        when(client.execute(anyString())).thenReturn(mapper.createArrayNode());
        assertFalse((boolean) repositoryClass.getMethod("existsByNombre", String.class)
                .invoke(repository, "Ausente"));
        assertTrue(((java.util.Optional<?>) repositoryClass.getMethod("findById", UUID.class)
                .invoke(repository, ID)).isEmpty());
        assertTrue(((List<?>) repositoryClass.getMethod("findAll").invoke(repository)).isEmpty());

        when(client.execute(anyString())).thenReturn(response(record(table, true)));
        final var invalid = ((java.util.Optional<?>) repositoryClass.getMethod("findById", UUID.class)
                .invoke(repository, ID)).orElseThrow();
        final Method getId = entityClass.getMethod("getId");
        assertEquals(new UUID(0, 0), getId.invoke(invalid));
    }

    private ObjectNode record(final String table, final boolean invalid) {
        return mapper.createObjectNode()
                .put("id", invalid ? "invalid" : table + ":`" + ID + "`")
                .put("nombre", "Catalogo");
    }

    private tools.jackson.databind.JsonNode response(final ObjectNode record) {
        final var response = mapper.createArrayNode();
        response.addObject().putArray("result");
        response.addObject().putArray("result").add(record);
        return response;
    }
}
