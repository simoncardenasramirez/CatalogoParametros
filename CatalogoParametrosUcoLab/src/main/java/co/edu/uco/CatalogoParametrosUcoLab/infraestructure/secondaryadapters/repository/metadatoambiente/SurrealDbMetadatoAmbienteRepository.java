package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.metadatoambiente;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.JsonNodeFactory;

@Repository
public final class SurrealDbMetadatoAmbienteRepository implements MetadatoAmbienteRepository {
    private static final String TABLE_NAME = "metadatos_ambiente";
    private final SurrealDbClient surrealDbClient;

    public SurrealDbMetadatoAmbienteRepository(final SurrealDbClient surrealDbClient) {
        this.surrealDbClient = surrealDbClient;
    }

    @Override public MetadatoAmbienteEntity save(final MetadatoAmbienteEntity entity) {
        surrealDbClient.execute(writeQuery("CREATE", entity));
        return entity;
    }

    @Override public MetadatoAmbienteEntity update(final MetadatoAmbienteEntity entity) {
        surrealDbClient.execute(writeQuery("UPDATE", entity));
        return entity;
    }

    @Override public void deleteById(final UUID id) {
        surrealDbClient.execute("""
                BEGIN TRANSACTION;
                DELETE type::record('%s', '%s');
                COMMIT TRANSACTION;
                """.formatted(TABLE_NAME, id));
    }

    @Override public Optional<MetadatoAmbienteEntity> findById(final UUID id) {
        var nodes = firstStatementResult(
                surrealDbClient.execute("SELECT * FROM %s:`%s`;".formatted(TABLE_NAME, id)));
        return nodes.isArray() && !nodes.isEmpty() ? Optional.of(toEntity(nodes.get(0))) : Optional.empty();
    }

    @Override public List<MetadatoAmbienteEntity> findAll() {
        return query("SELECT * FROM " + TABLE_NAME + ";");
    }

    @Override public List<MetadatoAmbienteEntity> findAllPaginado(final int pagina, final int tamanoPagina) {
        var offset = (pagina - 1) * tamanoPagina;
        return query("SELECT * FROM %s LIMIT %d START %d;".formatted(TABLE_NAME, tamanoPagina, offset));
    }

    @Override public boolean existsByIdAmbiente(final UUID idAmbiente) {
        return exists("idAmbiente", idAmbiente);
    }

    @Override public boolean existsByIdEstadoMetadatoAmbiente(final UUID idEstado) {
        return exists("idEstadoMetadatoAmbiente", idEstado);
    }

    private boolean exists(final String field, final UUID id) {
        var nodes = firstStatementResult(surrealDbClient.execute("SELECT id FROM %s WHERE %s = '%s' LIMIT 1;"
                .formatted(TABLE_NAME, field, id)));
        return nodes.isArray() && !nodes.isEmpty();
    }

    private String writeQuery(final String operation, final MetadatoAmbienteEntity entity) {
        return """
                BEGIN TRANSACTION;
                %s type::record('%s', '%s') CONTENT {
                    idParametro: '%s',
                    idAmbiente: '%s',
                    idEstadoMetadatoAmbiente: '%s'
                };
                COMMIT TRANSACTION;
                """.formatted(operation, TABLE_NAME, entity.getId(), entity.getIdParametro(),
                        entity.getIdAmbiente(), entity.getIdEstadoMetadatoAmbiente());
    }

    private List<MetadatoAmbienteEntity> query(final String sql) {
        var entities = new ArrayList<MetadatoAmbienteEntity>();
        var nodes = firstStatementResult(surrealDbClient.execute(sql));
        if (nodes.isArray()) nodes.forEach(node -> entities.add(toEntity(node)));
        return entities;
    }

    private JsonNode firstStatementResult(final JsonNode response) {
        if (!response.isArray() || response.isEmpty()) {
            return JsonNodeFactory.instance.arrayNode();
        }
        return response.get(response.size() - 1).path("result");
    }

    private MetadatoAmbienteEntity toEntity(final JsonNode node) {
        return MetadatoAmbienteEntity.create(extractUuid(node.path("id")),
                extractUuid(node.path("idParametro")), extractUuid(node.path("idAmbiente")),
                extractUuid(node.path("idEstadoMetadatoAmbiente")));
    }

    private UUID extractUuid(final JsonNode idNode) {
        var value = idNode.asString().replace("`", "").replace("u'", "").replace("'", "");
        final var separator = value.indexOf(':');
        if (separator >= 0 && separator < value.length() - 1) {
            value = value.substring(separator + 1);
        }
        if (TextHelper.isBlank(value)) {
            return UUIDHelper.getDefault();
        }
        try {
            return UUID.fromString(value);
        } catch (final IllegalArgumentException exception) {
            return UUIDHelper.getDefault();
        }
    }
}
