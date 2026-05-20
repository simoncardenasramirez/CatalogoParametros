package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;
import tools.jackson.databind.JsonNode;

@Repository
public class SurrealDbParametroRepository implements ParametroRepository {

    private static final String TABLE_NAME = "parametros";

    private final SurrealDbClient surrealDbClient;

    public SurrealDbParametroRepository(final SurrealDbClient surrealDbClient) {
        this.surrealDbClient = surrealDbClient;
    }

    @Override
    public ParametroEntity save(final ParametroEntity parametro) {
        var query = """
                BEGIN TRANSACTION;
                CREATE type::record('%s', '%s') CONTENT {
                    nombre: '%s',
                    idFuncionalidad: '%s',
                    idTipoParametro: '%s',
                    activo: %s
                };
                COMMIT TRANSACTION;
                """.formatted(TABLE_NAME, parametro.getId(), escape(parametro.getNombre()),
                parametro.getIdFuncionalidad(), parametro.getIdTipoParametro(), parametro.isActivo());

        surrealDbClient.execute(query);
        return parametro;
    }

    @Override
    public boolean existsByNombre(final String nombre) {
        var query = "SELECT id FROM %s WHERE nombre = '%s' LIMIT 1;"
                .formatted(TABLE_NAME, escape(nombre));
        var result = firstStatementResult(surrealDbClient.execute(query));
        return result.isArray() && result.size() > 0;
    }

    @Override
    public Optional<ParametroEntity> findById(final UUID id) {
        var query = "SELECT * FROM type::record('%s', '%s');".formatted(TABLE_NAME, id);
        var result = firstStatementResult(surrealDbClient.execute(query));
        if (!result.isArray() || result.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(toEntity(result.get(0)));
    }

    @Override
    public List<ParametroEntity> findAll() {
        var result = firstStatementResult(surrealDbClient.execute("SELECT * FROM " + TABLE_NAME + ";"));
        var parametros = new ArrayList<ParametroEntity>();
        if (result.isArray()) {
            for (var item : result) {
                parametros.add(toEntity(item));
            }
        }
        return parametros;
    }

    private JsonNode firstStatementResult(final JsonNode response) {
        return response.get(response.size() - 1).path("result");
    }

    private ParametroEntity toEntity(final JsonNode node) {
        return ParametroEntity.create(extractUuid(node.path("id")), node.path("nombre").asText(),
                extractUuid(node.path("idFuncionalidad")), extractUuid(node.path("idTipoParametro")),
                node.path("activo").asBoolean());
    }

    private UUID extractUuid(final JsonNode idNode) {
        var value = idNode.asText();
        var separatorIndex = value.indexOf(':');
        if (separatorIndex >= 0 && separatorIndex < value.length() - 1) {
            value = value.substring(separatorIndex + 1);
        }
        value = value.replace("`", "");
        if (TextHelper.isBlank(value)) {
            return UUIDHelper.getDefault();
        }
        return UUID.fromString(value);
    }

    private String escape(final String value) {
        return value.replace("\\", "\\\\").replace("'", "\\'");
    }
}
