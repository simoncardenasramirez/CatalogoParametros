package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.estadometadatoambiente;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.JsonNodeFactory;

@Repository
public final class SurrealDbEstadoMetadatoAmbienteRepository implements EstadoMetadatoAmbienteRepository {
    private static final String TABLE_NAME = "estados_metadato_ambiente";
    private final SurrealDbClient surrealDbClient;

    public SurrealDbEstadoMetadatoAmbienteRepository(final SurrealDbClient surrealDbClient) {
        this.surrealDbClient = surrealDbClient;
    }

    @Override
    public EstadoMetadatoAmbienteEntity save(final EstadoMetadatoAmbienteEntity entity) {
        final var query = """
                BEGIN TRANSACTION;
                CREATE type::record('%s', '%s') CONTENT {
                    nombre: '%s'
                };
                COMMIT TRANSACTION;
                """.formatted(TABLE_NAME, entity.getId(), escape(entity.getNombre()));
        surrealDbClient.execute(query);
        return entity;
    }

    @Override
    public EstadoMetadatoAmbienteEntity update(final EstadoMetadatoAmbienteEntity entity) {
        final var query = """
                BEGIN TRANSACTION;
                UPDATE type::record('%s', '%s') CONTENT {
                    nombre: '%s'
                };
                COMMIT TRANSACTION;
                """.formatted(TABLE_NAME, entity.getId(), escape(entity.getNombre()));
        surrealDbClient.execute(query);
        return entity;
    }

    @Override
    public void deleteById(final UUID id) {
        surrealDbClient.execute("""
                BEGIN TRANSACTION;
                DELETE type::record('%s', '%s');
                COMMIT TRANSACTION;
                """.formatted(TABLE_NAME, id));
    }

    @Override
    public boolean existsByNombre(final String nombre) {
        final var query = "SELECT id FROM %s WHERE nombre = '%s' LIMIT 1;"
                .formatted(TABLE_NAME, escape(nombre));
        final var result = firstStatementResult(surrealDbClient.execute(query));
        return result.isArray() && !result.isEmpty();
    }

    @Override
    public Optional<EstadoMetadatoAmbienteEntity> findById(final UUID id) {
        final var result = firstStatementResult(
                surrealDbClient.execute("SELECT * FROM %s:`%s`;".formatted(TABLE_NAME, id)));
        if (!result.isArray() || result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(toEntity(result.get(0)));
    }

    @Override
    public List<EstadoMetadatoAmbienteEntity> findAll() {
        return query("SELECT * FROM " + TABLE_NAME + ";");
    }

    @Override
    public List<EstadoMetadatoAmbienteEntity> findAllPaginado(final int pagina, final int tamanoPagina) {
        final var offset = (pagina - 1) * tamanoPagina;
        return query("SELECT * FROM %s LIMIT %d START %d;"
                .formatted(TABLE_NAME, tamanoPagina, offset));
    }

    private List<EstadoMetadatoAmbienteEntity> query(final String sql) {
        final var entities = new ArrayList<EstadoMetadatoAmbienteEntity>();
        final var result = firstStatementResult(surrealDbClient.execute(sql));
        if (result.isArray()) {
            result.forEach(node -> entities.add(toEntity(node)));
        }
        return entities;
    }

    private JsonNode firstStatementResult(final JsonNode response) {
        if (!response.isArray() || response.isEmpty()) {
            return JsonNodeFactory.instance.arrayNode();
        }
        return response.get(response.size() - 1).path("result");
    }

    private EstadoMetadatoAmbienteEntity toEntity(final JsonNode node) {
        return EstadoMetadatoAmbienteEntity.create(extractUuid(node.path("id")), node.path("nombre").asString());
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

    private String escape(final String value) {
        return value.replace("\\", "\\\\").replace("'", "\\'");
    }
}
