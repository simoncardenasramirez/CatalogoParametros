package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.modulo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.DateTimeHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;
import tools.jackson.databind.JsonNode;

@Repository
public class SurrealDbModuloRepository implements ModuloRepository {

    private static final String TABLE_NAME = "modulos";

    private final SurrealDbClient surrealDbClient;

    public SurrealDbModuloRepository(final SurrealDbClient surrealDbClient) {
        this.surrealDbClient = surrealDbClient;
    }

    @Override
    public ModuloEntity update(final ModuloEntity modulo) {
        var query = """
                BEGIN TRANSACTION;
                UPDATE type::record('%s', '%s') CONTENT {
                    nombre: '%s',
                    idAplicacion: '%s',
                    activo: %s,
                    fechaInicio: %s,
                    fechaFinal: %s
                };
                COMMIT TRANSACTION;
                """.formatted(TABLE_NAME, modulo.getId(), escape(modulo.getNombre()),
                modulo.getIdAplicacion(), modulo.isActivo(),
                DateTimeHelper.format(modulo.getFechaInicio()),
                DateTimeHelper.format(modulo.getFechaFinal()));

        surrealDbClient.execute(query);
        return modulo;
    }

    @Override
    public ModuloEntity save(final ModuloEntity modulo) {
        var query = """
                BEGIN TRANSACTION;
                CREATE type::record('%s', '%s') CONTENT {
                    nombre: '%s',
                    idAplicacion: '%s',
                    activo: %s,
                    fechaInicio: %s,
                    fechaFinal: %s
                };
                COMMIT TRANSACTION;
                """.formatted(TABLE_NAME, modulo.getId(), escape(modulo.getNombre()),
                modulo.getIdAplicacion(), modulo.isActivo(),
                DateTimeHelper.format(modulo.getFechaInicio()),
                DateTimeHelper.format(modulo.getFechaFinal()));

        surrealDbClient.execute(query);
        return modulo;
    }

    @Override
    public void deleteById(final UUID id) {
        surrealDbClient.execute("DELETE type::record('%s', '%s');".formatted(TABLE_NAME, id));
    }

    @Override
    public boolean existsByNombre(final String nombre) {
        var query = "SELECT id FROM %s WHERE nombre = '%s' LIMIT 1;"
                .formatted(TABLE_NAME, escape(nombre));
        var result = firstStatementResult(surrealDbClient.execute(query));
        return result.isArray() && result.size() > 0;
    }

    @Override
    public boolean existsByIdAplicacion(final UUID idAplicacion) {
        var query = "SELECT id FROM %s WHERE idAplicacion = '%s' LIMIT 1;"
                .formatted(TABLE_NAME, idAplicacion);
        var result = firstStatementResult(surrealDbClient.execute(query));
        return result.isArray() && result.size() > 0;
    }

    @Override
    public Optional<ModuloEntity> findById(final UUID id) {
        var query = "SELECT * FROM %s:`%s`;".formatted(TABLE_NAME, id);
        var result = firstStatementResult(surrealDbClient.execute(query));
        if (!result.isArray() || result.size() == 0) {
            return Optional.empty();
        }
        return Optional.of(toEntity(result.get(0)));
    }

    @Override
    public List<ModuloEntity> findAll() {
        var result = firstStatementResult(surrealDbClient.execute("SELECT * FROM " + TABLE_NAME + ";"));
        var modulos = new ArrayList<ModuloEntity>();
        if (result.isArray()) {
            for (var item : result) {
                try {
                    modulos.add(toEntity(item));
                } catch (final IllegalArgumentException exception) {
                    // Skip records with non-UUID IDs
                }
            }
        }
        return modulos;
    }

    @Override
    public List<ModuloEntity> findAllPaginado(final int pagina, final int tamanoPagina) {
        var offset = (pagina - 1) * tamanoPagina;
        var query = "SELECT * FROM " + TABLE_NAME + " LIMIT " + tamanoPagina + " START " + offset + ";";
        var result = firstStatementResult(surrealDbClient.execute(query));
        var modulos = new ArrayList<ModuloEntity>();
        if (result.isArray()) {
            for (var item : result) {
                try {
                    modulos.add(toEntity(item));
                } catch (final IllegalArgumentException exception) {
                    // Skip records with non-UUID IDs
                }
            }
        }
        return modulos;
    }

    private JsonNode firstStatementResult(final JsonNode response) {
        if (!response.isArray() || response.size() == 0) {
            return tools.jackson.databind.node.JsonNodeFactory.instance.arrayNode();
        }
        return response.get(response.size() - 1).path("result");
    }

    private ModuloEntity toEntity(final JsonNode node) {
        return ModuloEntity.create(
                extractUuid(node.path("id")),
                node.path("nombre").asText(),
                extractUuid(node.path("idAplicacion")),
                node.path("activo").asBoolean(),
                DateTimeHelper.parse(node.path("fechaInicio")),
                DateTimeHelper.parse(node.path("fechaFinal"))
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

    private String escape(final String value) {
        return value.replace("\\", "\\\\").replace("'", "\\'");
    }
}
