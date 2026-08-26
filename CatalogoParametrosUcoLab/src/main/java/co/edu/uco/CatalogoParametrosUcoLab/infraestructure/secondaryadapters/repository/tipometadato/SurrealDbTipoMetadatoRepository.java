package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.tipometadato;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoMetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TipoMetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;
import tools.jackson.databind.JsonNode;

@Repository
public final class SurrealDbTipoMetadatoRepository implements TipoMetadatoRepository {
    private static final String TABLE_NAME = "tipos_metadato";
    public static final UUID JSON_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    public static final UUID ALFANUMERICO_ID = UUID.fromString("00000000-0000-0000-0000-000000000002");
    public static final UUID DATE_ID = UUID.fromString("00000000-0000-0000-0000-000000000003");
    private final SurrealDbClient surrealDbClient;

    public SurrealDbTipoMetadatoRepository(final SurrealDbClient surrealDbClient) {
        this.surrealDbClient = surrealDbClient;
    }

    @Override
    public Optional<TipoMetadatoEntity> findById(final UUID id) {
        seedCatalog();
        var result = firstResult(surrealDbClient.execute("SELECT * FROM %s:`%s`;".formatted(TABLE_NAME, id)));
        return result.isArray() && !result.isEmpty() ? Optional.of(toEntity(result.get(0))) : Optional.empty();
    }

    @Override
    public List<TipoMetadatoEntity> findAll() {
        seedCatalog();
        var result = firstResult(surrealDbClient.execute("SELECT * FROM " + TABLE_NAME + " ORDER BY id;"));
        var entities = new ArrayList<TipoMetadatoEntity>();
        if (result.isArray()) result.forEach(node -> entities.add(toEntity(node)));
        return entities;
    }

    private void seedCatalog() {
        surrealDbClient.execute("""
                UPSERT type::record('%s', '%s') CONTENT { tipo: 'Json', detalle: 'Es un documento digital creado en este lenguaje que almacena información organizada' };
                UPSERT type::record('%s', '%s') CONTENT { tipo: 'alfanumerico', detalle: 'Tipo de datos que contiene tanto letras como números' };
                UPSERT type::record('%s', '%s') CONTENT { tipo: 'date', detalle: 'Tipo de dato que contiene fechas' };
                """.formatted(TABLE_NAME, JSON_ID, TABLE_NAME, ALFANUMERICO_ID, TABLE_NAME, DATE_ID));
    }

    private JsonNode firstResult(final JsonNode response) {
        return response.isArray() && !response.isEmpty()
                ? response.get(response.size() - 1).path("result")
                : tools.jackson.databind.node.JsonNodeFactory.instance.arrayNode();
    }

    private TipoMetadatoEntity toEntity(final JsonNode node) {
        var rawId = node.path("id").asText().replace("`", "").replace("u'", "").replace("'", "");
        rawId = rawId.substring(rawId.indexOf(':') + 1);
        return TipoMetadatoEntity.create(UUID.fromString(rawId), node.path("tipo").asText(), node.path("detalle").asText());
    }
}
