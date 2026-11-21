package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.identidadservicio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.IdentidadServicioEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.IdentidadServicioRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.JsonNodeFactory;

@Repository
public final class SurrealDbIdentidadServicioRepository implements IdentidadServicioRepository {
    private static final String TABLE_NAME = "identidades_servicio";
    private final SurrealDbClient surrealDbClient;

    public SurrealDbIdentidadServicioRepository(final SurrealDbClient surrealDbClient) {
        this.surrealDbClient = surrealDbClient;
    }

    @Override
    public IdentidadServicioEntity save(final IdentidadServicioEntity entity) {
        surrealDbClient.execute("""
                BEGIN TRANSACTION;
                CREATE type::record('%s', '%s') CONTENT {
                    idAplicacion: '%s',
                    idAmbiente: '%s',
                    clientId: '%s'
                };
                COMMIT TRANSACTION;
                """.formatted(TABLE_NAME, entity.getId(), entity.getIdAplicacion(), entity.getIdAmbiente(),
                escape(entity.getClientId())));
        return entity;
    }

    @Override
    public List<IdentidadServicioEntity> findByIdAplicacion(final UUID idAplicacion) {
        return query("SELECT * FROM %s WHERE idAplicacion = '%s' ORDER BY idAmbiente;"
                .formatted(TABLE_NAME, idAplicacion));
    }

    @Override
    public Optional<IdentidadServicioEntity> findByClientId(final String clientId) {
        final var result = query("SELECT * FROM %s WHERE clientId = '%s' LIMIT 1;"
                .formatted(TABLE_NAME, escape(clientId)));
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public Optional<IdentidadServicioEntity> findByIdAplicacionAndClientId(final UUID idAplicacion,
            final String clientId) {
        final var result = query("SELECT * FROM %s WHERE idAplicacion = '%s' AND clientId = '%s' LIMIT 1;"
                .formatted(TABLE_NAME, idAplicacion, escape(clientId)));
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public void deleteById(final UUID id) {
        surrealDbClient.execute("""
                BEGIN TRANSACTION;
                DELETE type::record('%s', '%s');
                COMMIT TRANSACTION;
                """.formatted(TABLE_NAME, id));
    }

    private List<IdentidadServicioEntity> query(final String sql) {
        final var entities = new ArrayList<IdentidadServicioEntity>();
        final var nodes = firstStatementResult(surrealDbClient.execute(sql));
        if (nodes.isArray()) nodes.forEach(node -> entities.add(toEntity(node)));
        return entities;
    }

    private JsonNode firstStatementResult(final JsonNode response) {
        if (!response.isArray() || response.isEmpty()) return JsonNodeFactory.instance.arrayNode();
        return response.get(response.size() - 1).path("result");
    }

    private IdentidadServicioEntity toEntity(final JsonNode node) {
        return IdentidadServicioEntity.create(extractUuid(node.path("id")),
                extractUuid(node.path("idAplicacion")), extractUuid(node.path("idAmbiente")),
                node.path("clientId").asString());
    }

    private UUID extractUuid(final JsonNode node) {
        var value = node.asString().replace("`", "").replace("u'", "").replace("'", "");
        final var separator = value.indexOf(':');
        if (separator >= 0 && separator < value.length() - 1) value = value.substring(separator + 1);
        if (TextHelper.isBlank(value)) return UUIDHelper.getDefault();
        try { return UUID.fromString(value); } catch (final IllegalArgumentException exception) { return UUIDHelper.getDefault(); }
    }

    private String escape(final String value) {
        return value.replace("\\", "\\\\").replace("'", "\\'");
    }
}
