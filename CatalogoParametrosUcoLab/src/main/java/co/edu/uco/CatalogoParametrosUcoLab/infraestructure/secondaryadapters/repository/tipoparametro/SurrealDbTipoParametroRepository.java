package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.tipoparametro;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TipoParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;
import tools.jackson.databind.JsonNode;

@Repository
public class SurrealDbTipoParametroRepository implements TipoParametroRepository {

    private static final String TABLE_NAME = "tipos_parametro";

    private final SurrealDbClient surrealDbClient;

    public SurrealDbTipoParametroRepository(final SurrealDbClient surrealDbClient) {
        this.surrealDbClient = surrealDbClient;
    }

    @Override
    public TipoParametroEntity save(final TipoParametroEntity tipoParametro) {
        var query = """
                BEGIN TRANSACTION;
                CREATE type::record('%s', '%s') CONTENT {
                    nombre: '%s'
                };
                COMMIT TRANSACTION;
                """.formatted(TABLE_NAME, tipoParametro.getId(), escape(tipoParametro.getNombre()));

        surrealDbClient.execute(query);
        return tipoParametro;
    }

    @Override
    public boolean existsByNombre(final String nombre) {
        var query = "SELECT id FROM %s WHERE nombre = '%s' LIMIT 1;"
                .formatted(TABLE_NAME, escape(nombre));
        var result = firstStatementResult(surrealDbClient.execute(query));
        return result.isArray() && result.size() > 0;
    }

    @Override
    public Optional<TipoParametroEntity> findById(final UUID id) {
        var query = "SELECT * FROM %s:`%s`;".formatted(TABLE_NAME, id);
        var result = firstStatementResult(surrealDbClient.execute(query));
        if (!result.isArray() || result.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(toEntity(result.get(0)));
    }

    @Override
    public List<TipoParametroEntity> findAll() {
        var result = firstStatementResult(surrealDbClient.execute("SELECT * FROM " + TABLE_NAME + ";"));
        var tiposParametro = new ArrayList<TipoParametroEntity>();
        if (result.isArray()) {
            for (var item : result) {
                try {
                    tiposParametro.add(toEntity(item));
                } catch (final IllegalArgumentException exception) {
                    // Skip records with non-UUID IDs
                }
            }
        }
        return tiposParametro;
    }

    @Override
    public TipoParametroEntity update(final TipoParametroEntity tipoParametro) {
        var query = """
                BEGIN TRANSACTION;
                UPDATE type::record('%s', '%s') CONTENT {
                    nombre: '%s'
                };
                COMMIT TRANSACTION;
                """.formatted(TABLE_NAME, tipoParametro.getId(), escape(tipoParametro.getNombre()));

        surrealDbClient.execute(query);
        return tipoParametro;
    }

    @Override
    public void deleteById(final UUID id) {
        var query = "DELETE type::record('%s', '%s');".formatted(TABLE_NAME, id);
        surrealDbClient.execute(query);
    }

    private JsonNode firstStatementResult(final JsonNode response) {
        if (!response.isArray() || response.size() == 0) {
            return tools.jackson.databind.node.JsonNodeFactory.instance.arrayNode();
        }
        return response.get(response.size() - 1).path("result");
    }

    private TipoParametroEntity toEntity(final JsonNode node) {
        return TipoParametroEntity.create(
                extractUuid(node.path("id")),
                node.path("nombre").asText()
        );
    }

    private UUID extractUuid(final JsonNode idNode) {
        var value = idNode.asText();
        var separatorIndex = value.indexOf(':');
        if (separatorIndex >= 0 && separatorIndex < value.length() - 1) {
            value = value.substring(separatorIndex + 1);
        }
        value = value.replace("`", "").replace("u'", "").replace("'", "");
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
