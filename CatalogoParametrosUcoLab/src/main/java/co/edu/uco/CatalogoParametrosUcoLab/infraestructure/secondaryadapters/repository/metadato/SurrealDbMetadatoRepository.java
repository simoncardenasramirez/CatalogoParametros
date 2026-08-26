package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.metadato;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;
import tools.jackson.databind.JsonNode;

@Repository
public final class SurrealDbMetadatoRepository implements MetadatoRepository {
    private static final String TABLE = "metadatos";
    private final SurrealDbClient client;
    public SurrealDbMetadatoRepository(final SurrealDbClient client) { this.client = client; }

    @Override public MetadatoEntity save(final MetadatoEntity entity) {
        client.execute("""
                CREATE type::record('%s', '%s') CONTENT {
                    idParametro: '%s', idTipoMetadato: '%s', valor: %s
                };
                """.formatted(TABLE, entity.getId(), entity.getIdParametro(), entity.getIdTipoMetadato(),
                serialize(entity.getValor())));
        return entity;
    }
    @Override public MetadatoEntity update(final MetadatoEntity entity) {
        client.execute("""
                UPDATE type::record('%s', '%s') CONTENT {
                    idParametro: '%s', idTipoMetadato: '%s', valor: %s
                };
                """.formatted(TABLE, entity.getId(), entity.getIdParametro(), entity.getIdTipoMetadato(),
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
        return MetadatoEntity.create(uuid(node.path("id").asText()), UUID.fromString(node.path("idParametro").asText()),
                UUID.fromString(node.path("idTipoMetadato").asText()), node.path("valor").deepCopy());
    }
    private UUID uuid(final String recordId) {
        var value = recordId.replace("`", "").replace("u'", "").replace("'", "");
        return UUID.fromString(value.substring(value.indexOf(':') + 1));
    }
    private String serialize(final JsonNode value) { return value == null ? "null" : value.toString(); }
}
