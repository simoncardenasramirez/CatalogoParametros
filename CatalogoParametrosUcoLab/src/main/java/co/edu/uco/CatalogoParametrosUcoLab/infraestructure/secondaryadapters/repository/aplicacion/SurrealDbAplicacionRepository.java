package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.aplicacion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;
import tools.jackson.databind.JsonNode;

@Repository
public class SurrealDbAplicacionRepository implements AplicacionRepository {

    private static final String TABLE_NAME = "aplicaciones";

    private final SurrealDbClient surrealDbClient;

    public SurrealDbAplicacionRepository(final SurrealDbClient surrealDbClient) {
        this.surrealDbClient = surrealDbClient;
    }

    @Override
    public AplicacionEntity update(final AplicacionEntity aplicacion) {
        var query = """
                BEGIN TRANSACTION;
                UPDATE type::record('%s', '%s') CONTENT {
                    nombre: '%s',
                    idOrganizacion: '%s',
                    activa: %s,
                    fechaInicio: %s,
                    fechaFinal: %s
                };
                COMMIT TRANSACTION;
                """.formatted(TABLE_NAME, aplicacion.getId(), escape(aplicacion.getNombre()),
                aplicacion.getIdOrganizacion(), aplicacion.isActiva(),
                formatDateTime(aplicacion.getFechaInicio()),
                formatDateTime(aplicacion.getFechaFinal()));

        surrealDbClient.execute(query);
        return aplicacion;
    }

    @Override
    public AplicacionEntity save(final AplicacionEntity aplicacion) {
        var query = """
                BEGIN TRANSACTION;
                CREATE type::record('%s', '%s') CONTENT {
                    nombre: '%s',
                    idOrganizacion: '%s',
                    activa: %s,
                    fechaInicio: %s,
                    fechaFinal: %s
                };
                COMMIT TRANSACTION;
                """.formatted(TABLE_NAME, aplicacion.getId(), escape(aplicacion.getNombre()),
                aplicacion.getIdOrganizacion(), aplicacion.isActiva(),
                formatDateTime(aplicacion.getFechaInicio()),
                formatDateTime(aplicacion.getFechaFinal()));

        surrealDbClient.execute(query);
        return aplicacion;
    }

    @Override
    public boolean existsByNombre(final String nombre) {
        var query = "SELECT id FROM %s WHERE nombre = '%s' LIMIT 1;"
                .formatted(TABLE_NAME, escape(nombre));
        var result = firstStatementResult(surrealDbClient.execute(query));
        return result.isArray() && result.size() > 0;
    }

    @Override
    public Optional<AplicacionEntity> findById(final UUID id) {
        var query = "SELECT * FROM %s:`%s`;".formatted(TABLE_NAME, id);
        var result = firstStatementResult(surrealDbClient.execute(query));
        if (!result.isArray() || result.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(toEntity(result.get(0)));
    }

    @Override
    public List<AplicacionEntity> findAll() {
        var result = firstStatementResult(surrealDbClient.execute("SELECT * FROM " + TABLE_NAME + ";"));
        var aplicaciones = new ArrayList<AplicacionEntity>();
        if (result.isArray()) {
            for (var item : result) {
                try {
                    aplicaciones.add(toEntity(item));
                } catch (final IllegalArgumentException exception) {
                    // Skip records with non-UUID IDs
                }
            }
        }
        return aplicaciones;
    }

    @Override
    public boolean existsByIdOrganizacion(final UUID idOrganizacion) {
        var query = "SELECT id FROM %s WHERE idOrganizacion = '%s' LIMIT 1;"
                .formatted(TABLE_NAME, idOrganizacion);
        var result = firstStatementResult(surrealDbClient.execute(query));
        return result.isArray() && result.size() > 0;
    }

    private JsonNode firstStatementResult(final JsonNode response) {
        if (!response.isArray() || response.size() == 0) {
            return tools.jackson.databind.node.JsonNodeFactory.instance.arrayNode();
        }
        return response.get(response.size() - 1).path("result");
    }

    private AplicacionEntity toEntity(final JsonNode node) {
        return AplicacionEntity.create(
                extractUuid(node.path("id")),
                node.path("nombre").asText(),
                extractUuid(node.path("idOrganizacion")),
                node.path("activa").asBoolean(),
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
