package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.estadotoken;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoTokenEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosEstadoTokenRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;
import tools.jackson.databind.JsonNode;

@Repository
public final class SurrealDbEstadoTokenRepository implements BaseDatosEstadoTokenRepository {
    private static final String TABLE_NAME = "estados_token";
    public static final UUID ACTIVO_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    public static final UUID INACTIVO_ID = UUID.fromString("00000000-0000-0000-0000-000000000002");
    public static final UUID REVOCADO_ID = UUID.fromString("00000000-0000-0000-0000-000000000003");
    private final SurrealDbClient surrealDbClient;

    public SurrealDbEstadoTokenRepository(final SurrealDbClient surrealDbClient) {
        this.surrealDbClient = surrealDbClient;
    }

    @Override
    public Optional<EstadoTokenEntity> findById(final UUID id) {
        seedCatalog();
        var result = firstResult(surrealDbClient.execute("SELECT * FROM %s:`%s`;".formatted(TABLE_NAME, id)));
        return result.isArray() && !result.isEmpty() ? Optional.of(toEntity(result.get(0))) : Optional.empty();
    }

    @Override
    public List<EstadoTokenEntity> findAll() {
        seedCatalog();
        var result = firstResult(surrealDbClient.execute("SELECT * FROM " + TABLE_NAME + " ORDER BY id;"));
        var entities = new ArrayList<EstadoTokenEntity>();
        if (result.isArray()) result.forEach(node -> entities.add(toEntity(node)));
        return entities;
    }

    private void seedCatalog() {
        surrealDbClient.execute("""
                UPSERT type::record('%s', '%s') CONTENT { estado: 'activo' };
                UPSERT type::record('%s', '%s') CONTENT { estado: 'inactivo' };
                UPSERT type::record('%s', '%s') CONTENT { estado: 'revocado' };
                """.formatted(TABLE_NAME, ACTIVO_ID, TABLE_NAME, INACTIVO_ID, TABLE_NAME, REVOCADO_ID));
    }

    private JsonNode firstResult(final JsonNode response) {
        return response.isArray() && !response.isEmpty()
                ? response.get(response.size() - 1).path("result")
                : tools.jackson.databind.node.JsonNodeFactory.instance.arrayNode();
    }

    private EstadoTokenEntity toEntity(final JsonNode node) {
        var rawId = node.path("id").asString().replace("`", "").replace("u'", "").replace("'", "");
        rawId = rawId.substring(rawId.indexOf(':') + 1);
        return EstadoTokenEntity.create(UUID.fromString(rawId), node.path("estado").asString());
    }
}
