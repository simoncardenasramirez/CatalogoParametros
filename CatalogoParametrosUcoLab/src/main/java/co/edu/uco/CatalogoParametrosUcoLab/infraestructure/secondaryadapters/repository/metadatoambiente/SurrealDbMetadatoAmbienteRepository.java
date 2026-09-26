package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.metadatoambiente;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.JsonNodeFactory;

@Repository
public final class SurrealDbMetadatoAmbienteRepository implements MetadatoAmbienteRepository {
    private static final String TABLE = "metadatos_ambiente";
    private final SurrealDbClient client;

    public SurrealDbMetadatoAmbienteRepository(final SurrealDbClient client) { this.client = client; }

    @Override public MetadatoAmbienteEntity save(final MetadatoAmbienteEntity entity) {
        client.execute(writeQuery("CREATE", entity));
        return entity;
    }

    @Override public MetadatoAmbienteEntity update(final MetadatoAmbienteEntity entity) {
        client.execute(writeQuery("UPDATE", entity));
        return entity;
    }

    @Override public void deleteById(final UUID id) {
        client.execute("DELETE type::record('%s', '%s');".formatted(TABLE, id));
    }

    @Override public Optional<MetadatoAmbienteEntity> findById(final UUID id) {
        var nodes = result(client.execute("SELECT * FROM %s:`%s`;".formatted(TABLE, id)));
        return nodes.isArray() && !nodes.isEmpty() ? Optional.of(toEntity(nodes.get(0))) : Optional.empty();
    }

    @Override public List<MetadatoAmbienteEntity> findAll() { return query("SELECT * FROM " + TABLE + ";"); }

    @Override public List<MetadatoAmbienteEntity> findAllPaginado(final int pagina, final int tamanoPagina) {
        var offset = (pagina - 1) * tamanoPagina;
        return query("SELECT * FROM %s LIMIT %d START %d;".formatted(TABLE, tamanoPagina, offset));
    }

    @Override public boolean existsByIdAmbiente(final UUID idAmbiente) {
        return exists("idAmbiente", idAmbiente);
    }

    @Override public boolean existsByIdEstadoMetadatoAmbiente(final UUID idEstado) {
        return exists("idEstadoMetadatoAmbiente", idEstado);
    }

    private boolean exists(final String field, final UUID id) {
        var nodes = result(client.execute("SELECT id FROM %s WHERE %s = '%s' LIMIT 1;"
                .formatted(TABLE, field, id)));
        return nodes.isArray() && !nodes.isEmpty();
    }

    private String writeQuery(final String operation, final MetadatoAmbienteEntity entity) {
        return "%s type::record('%s', '%s') CONTENT { idParametro: '%s', idAmbiente: '%s', "
                .concat("idEstadoMetadatoAmbiente: '%s' };")
                .formatted(operation, TABLE, entity.getId(), entity.getIdParametro(), entity.getIdAmbiente(),
                        entity.getIdEstadoMetadatoAmbiente());
    }

    private List<MetadatoAmbienteEntity> query(final String sql) {
        var entities = new ArrayList<MetadatoAmbienteEntity>();
        var nodes = result(client.execute(sql));
        if (nodes.isArray()) nodes.forEach(node -> entities.add(toEntity(node)));
        return entities;
    }

    private JsonNode result(final JsonNode response) {
        return response.isArray() && !response.isEmpty()
                ? response.get(response.size() - 1).path("result") : JsonNodeFactory.instance.arrayNode();
    }

    private MetadatoAmbienteEntity toEntity(final JsonNode node) {
        return MetadatoAmbienteEntity.create(uuid(node.path("id").asString()),
                UUID.fromString(node.path("idParametro").asString()),
                UUID.fromString(node.path("idAmbiente").asString()),
                UUID.fromString(node.path("idEstadoMetadatoAmbiente").asString()));
    }

    private UUID uuid(final String recordId) {
        var value = recordId.replace("`", "").replace("u'", "").replace("'", "");
        return UUID.fromString(value.substring(value.indexOf(':') + 1));
    }
}
