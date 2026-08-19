package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.funcionalidad;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;
import tools.jackson.databind.JsonNode;

@Repository
public class SurrealDbFuncionalidadRepository implements FuncionalidadRepository {

    private static final String TABLE_NAME = "funcionalidades";

    private final SurrealDbClient surrealDbClient;

    public SurrealDbFuncionalidadRepository(final SurrealDbClient surrealDbClient) {
        this.surrealDbClient = surrealDbClient;
    }

    @Override
    public FuncionalidadEntity save(final FuncionalidadEntity funcionalidad) {
        var query = """
                BEGIN TRANSACTION;
                CREATE type::record('%s', '%s') CONTENT {
                    nombre: '%s',
                    idModulo: '%s',
                    activo: %s,
                    fechaInicio: %s,
                    fechaFinal: %s
                };
                COMMIT TRANSACTION;
                """.formatted(TABLE_NAME, funcionalidad.getId(), escape(funcionalidad.getNombre()),
                funcionalidad.getIdModulo(), funcionalidad.isActivo(),
                formatDateTime(funcionalidad.getFechaInicio()),
                formatDateTime(funcionalidad.getFechaFinal()));

        surrealDbClient.execute(query);
        return funcionalidad;
    }

    @Override
    public FuncionalidadEntity update(final FuncionalidadEntity funcionalidad) {
        var query = """
                BEGIN TRANSACTION;
                UPDATE type::record('%s', '%s') CONTENT {
                    nombre: '%s',
                    idModulo: '%s',
                    activo: %s,
                    fechaInicio: %s,
                    fechaFinal: %s
                };
                COMMIT TRANSACTION;
                """.formatted(TABLE_NAME, funcionalidad.getId(), escape(funcionalidad.getNombre()),
                funcionalidad.getIdModulo(), funcionalidad.isActivo(),
                formatDateTime(funcionalidad.getFechaInicio()),
                formatDateTime(funcionalidad.getFechaFinal()));

        surrealDbClient.execute(query);
        return funcionalidad;
    }

    @Override
    public void deleteById(final UUID id) {
        var query = """
                BEGIN TRANSACTION;
                DELETE type::record('%s', '%s');
                COMMIT TRANSACTION;
                """.formatted(TABLE_NAME, id);

        surrealDbClient.execute(query);
    }

    @Override
    public boolean existsByNombre(final String nombre) {
        var query = "SELECT id FROM %s WHERE nombre = '%s' LIMIT 1;"
                .formatted(TABLE_NAME, escape(nombre));
        var result = firstStatementResult(surrealDbClient.execute(query));
        return result.isArray() && result.size() > 0;
    }

    @Override
    public Optional<FuncionalidadEntity> findById(final UUID id) {
        var query = "SELECT * FROM %s:`%s`;".formatted(TABLE_NAME, id);
        var result = firstStatementResult(surrealDbClient.execute(query));
        if (!result.isArray() || result.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(toEntity(result.get(0)));
    }

    @Override
    public List<FuncionalidadEntity> findAll() {
        var result = firstStatementResult(surrealDbClient.execute("SELECT * FROM " + TABLE_NAME + ";"));
        var funcionalidades = new ArrayList<FuncionalidadEntity>();
        if (result.isArray()) {
            for (var item : result) {
                try {
                    funcionalidades.add(toEntity(item));
                } catch (final IllegalArgumentException exception) {
                    // Skip records with non-UUID IDs
                }
            }
        }
        return funcionalidades;
    }

    @Override
    public List<FuncionalidadEntity> findAllPaginado(final int pagina, final int tamanoPagina) {
        var offset = (pagina - 1) * tamanoPagina;
        var query = "SELECT * FROM " + TABLE_NAME + " LIMIT " + tamanoPagina + " START " + offset + ";";
        var result = firstStatementResult(surrealDbClient.execute(query));
        var funcionalidades = new ArrayList<FuncionalidadEntity>();
        if (result.isArray()) {
            for (var item : result) {
                try {
                    funcionalidades.add(toEntity(item));
                } catch (final IllegalArgumentException exception) {
                    // Skip records with non-UUID IDs
                }
            }
        }
        return funcionalidades;
    }

    private JsonNode firstStatementResult(final JsonNode response) {
        if (!response.isArray() || response.size() == 0) {
            return tools.jackson.databind.node.JsonNodeFactory.instance.arrayNode();
        }
        return response.get(response.size() - 1).path("result");
    }

    private FuncionalidadEntity toEntity(final JsonNode node) {
        return FuncionalidadEntity.create(
                extractUuid(node.path("id")),
                node.path("nombre").asText(),
                extractUuid(node.path("idModulo")),
                node.path("activo").asBoolean(),
                extractDateTime(node.path("fechaInicio")),
                extractDateTime(node.path("fechaFinal"))
        );
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
        try {
            return UUID.fromString(value);
        } catch (final IllegalArgumentException exception) {
            return UUIDHelper.getDefault();
        }
    }

    private LocalDateTime extractDateTime(final JsonNode dateNode) {
        if (dateNode.isNull() || TextHelper.isBlank(dateNode.asText())) {
            return null;
        }
        var text = dateNode.asText();
        // Remove SurrealDB date wrapper d'...'
        if (text.startsWith("d'") && text.endsWith("'")) {
            text = text.substring(2, text.length() - 1);
        }
        // Remove Z timezone suffix if present
        if (text.endsWith("Z")) {
            text = text.substring(0, text.length() - 1);
        }
        // Ensure seconds are present for LocalDateTime.parse
        if (text.length() == 16) { // yyyy-MM-ddTHH:mm
            text = text + ":00";
        }
        return LocalDateTime.parse(text);
    }

    private String formatDateTime(final LocalDateTime dateTime) {
        if (dateTime == null) {
            return "null";
        }
        return "'" + dateTime.toString() + "'";
    }

    private String escape(final String value) {
        return value.replace("\\", "\\\\").replace("'", "\\'");
    }
}