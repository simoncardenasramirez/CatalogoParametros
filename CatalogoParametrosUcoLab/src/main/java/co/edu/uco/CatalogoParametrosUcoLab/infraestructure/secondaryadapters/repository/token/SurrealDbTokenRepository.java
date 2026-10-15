package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.token;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TokenEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TokenRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;
import tools.jackson.databind.JsonNode;

@Repository
public final class SurrealDbTokenRepository implements TokenRepository {
    private static final String TABLE = "tokens";
    private final SurrealDbClient client;

    public SurrealDbTokenRepository(final SurrealDbClient client) { this.client = client; }

    @Override
    public TokenEntity save(final TokenEntity entity) {
        client.execute("""
                CREATE type::record('%s', '%s') CONTENT {
                    tokenHash: '%s', estadoToken: '%s', aplicacionId: '%s', ambienteId: '%s',
                    fechaCreacion: '%s', fechaExpiracion: '%s'
                };
                """.formatted(TABLE, entity.getId(), escape(entity.getTokenHash()), entity.getEstadoToken(),
                entity.getAplicacionId(), entity.getAmbienteId(), entity.getFechaCreacion(), entity.getFechaExpiracion()));
        return entity;
    }

    @Override
    public TokenEntity updateEstado(final UUID id, final UUID estadoToken) {
        client.execute("UPDATE type::record('%s', '%s') MERGE { estadoToken: '%s' };"
                .formatted(TABLE, id, estadoToken));
        return findById(id).orElseThrow();
    }

    @Override
    public Optional<TokenEntity> findById(final UUID id) {
        var nodes = result(client.execute("SELECT * FROM %s:`%s`;".formatted(TABLE, id)));
        return nodes.isArray() && !nodes.isEmpty() ? Optional.of(toEntity(nodes.get(0))) : Optional.empty();
    }

    @Override
    public List<TokenEntity> findAll() {
        var nodes = result(client.execute("SELECT * FROM " + TABLE + " ORDER BY fechaCreacion DESC;"));
        var entities = new ArrayList<TokenEntity>();
        if (nodes.isArray()) nodes.forEach(node -> entities.add(toEntity(node)));
        return entities;
    }

    private TokenEntity toEntity(final JsonNode node) {
        return TokenEntity.create(recordUuid(node.path("id").asString()), node.path("tokenHash").asString(),
                UUID.fromString(node.path("estadoToken").asString()), UUID.fromString(node.path("aplicacionId").asString()),
                UUID.fromString(node.path("ambienteId").asString()),
                LocalDateTime.parse(node.path("fechaCreacion").asString()),
                LocalDateTime.parse(node.path("fechaExpiracion").asString()));
    }

    private JsonNode result(final JsonNode response) {
        return response.isArray() && !response.isEmpty() ? response.get(response.size() - 1).path("result")
                : tools.jackson.databind.node.JsonNodeFactory.instance.arrayNode();
    }

    private UUID recordUuid(final String recordId) {
        var value = recordId.replace("`", "").replace("u'", "").replace("'", "");
        return UUID.fromString(value.substring(value.indexOf(':') + 1));
    }

    private String escape(final String value) { return value.replace("\\", "\\\\").replace("'", "\\'"); }
}
