package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.metadato;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.BaseDatosMetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;
import tools.jackson.databind.JsonNode;

@Repository
public final class SurrealDbMetadatoRepository implements BaseDatosMetadatoRepository {
    private static final String TABLE = "metadatos";
    private final SurrealDbClient client;
    public SurrealDbMetadatoRepository(final SurrealDbClient client) { this.client = client; }

    @Override public MetadatoEntity save(final MetadatoEntity entity) {
        client.execute("""
                BEGIN TRANSACTION;
                CREATE type::record('%s', '%s') CONTENT {
                    idParametro: '%s', idTipoMetadato: '%s', idAmbiente: '%s', valor: %s
                };
                COMMIT TRANSACTION;
                """.formatted(TABLE, entity.getId(), entity.getIdParametro(), entity.getIdTipoMetadato(), entity.getIdAmbiente(),
                serialize(entity.getValor())));
        return entity;
    }
    @Override public MetadatoEntity update(final MetadatoEntity entity) {
        client.execute("""
                BEGIN TRANSACTION;
                UPDATE type::record('%s', '%s') CONTENT {
                    idParametro: '%s', idTipoMetadato: '%s', idAmbiente: '%s', valor: %s
                };
                COMMIT TRANSACTION;
                """.formatted(TABLE, entity.getId(), entity.getIdParametro(), entity.getIdTipoMetadato(), entity.getIdAmbiente(),
                serialize(entity.getValor())));
        return entity;
    }
    @Override public void deleteById(final UUID id) {
        client.execute("DELETE type::record('%s', '%s');".formatted(TABLE, id));
    }
    @Override public Optional<MetadatoEntity> findById(final UUID id) {
        var result = result(client.execute("SELECT * FROM %s:`%s`;".formatted(TABLE, id)));
        return result.isArray() && !result.isEmpty() ? Optional.of(toEntity(result.get(0))) : Optional.empty();
    }
    @Override public List<MetadatoEntity> findAll() { return query("SELECT * FROM " + TABLE + ";"); }
    @Override public List<MetadatoEntity> findByIdParametro(final UUID id) {
        return query("SELECT * FROM %s WHERE idParametro = '%s';".formatted(TABLE, id));
    }
    @Override public List<MetadatoEntity> upsertAll(final List<MetadatoEntity> entities, final Set<UUID> existingIds) {
        if (entities.isEmpty()) return List.of();
        var query = new StringBuilder("BEGIN TRANSACTION;\n");
        for (var entity : entities) {
            var operation = existingIds.contains(entity.getId()) ? "UPDATE" : "CREATE";
            query.append(operation).append(" type::record('").append(TABLE).append("', '").append(entity.getId())
                    .append("') CONTENT { idParametro: '").append(entity.getIdParametro())
                    .append("', idTipoMetadato: '").append(entity.getIdTipoMetadato())
                    .append("', idAmbiente: '").append(entity.getIdAmbiente()).append("', valor: ")
                    .append(serialize(entity.getValor())).append(" };\n");
        }
        query.append("COMMIT TRANSACTION;");
        client.execute(query.toString());
        return entities;
    }
    private List<MetadatoEntity> query(final String sql) {
        var nodes = result(client.execute(sql));
        var entities = new ArrayList<MetadatoEntity>();
        if (nodes.isArray()) nodes.forEach(node -> entities.add(toEntity(node)));
        return entities;
    }
    private JsonNode result(final JsonNode response) {
        return response.isArray() && !response.isEmpty() ? response.get(response.size() - 1).path("result")
                : tools.jackson.databind.node.JsonNodeFactory.instance.arrayNode();
    }
    private MetadatoEntity toEntity(final JsonNode node) {
        return MetadatoEntity.create(uuid(node.path("id").asString()), UUID.fromString(node.path("idParametro").asString()),
                UUID.fromString(node.path("idTipoMetadato").asString()), ambiente(node), node.path("valor").deepCopy());
    }
    private UUID ambiente(final JsonNode node) {
        var valor = node.path("idAmbiente").asString("");
        try { return UUID.fromString(valor); } catch (IllegalArgumentException exception) { return null; }
    }
    private UUID uuid(final String recordId) {
        var value = recordId.replace("`", "").replace("u'", "").replace("'", "");
        return UUID.fromString(value.substring(value.indexOf(':') + 1));
    }
    private String serialize(final JsonNode value) { return value == null ? "null" : value.toString(); }
}
