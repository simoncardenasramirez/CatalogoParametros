package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;
import java.util.function.Function;

import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.JsonNodeFactory;

public abstract class AbstractNamedSurrealRepository<T> {
    private final SurrealDbClient client;
    private final String table;
    private final BiFunction<UUID, String, T> factory;
    private final Function<T, UUID> idGetter;
    private final Function<T, String> nameGetter;

    protected AbstractNamedSurrealRepository(final SurrealDbClient client, final String table,
            final BiFunction<UUID, String, T> factory, final Function<T, UUID> idGetter,
            final Function<T, String> nameGetter) {
        this.client = client;
        this.table = table;
        this.factory = factory;
        this.idGetter = idGetter;
        this.nameGetter = nameGetter;
    }

    public T save(final T entity) {
        client.execute("CREATE type::record('%s', '%s') CONTENT { nombre: '%s' };"
                .formatted(table, idGetter.apply(entity), escape(nameGetter.apply(entity))));
        return entity;
    }

    public T update(final T entity) {
        client.execute("UPDATE type::record('%s', '%s') CONTENT { nombre: '%s' };"
                .formatted(table, idGetter.apply(entity), escape(nameGetter.apply(entity))));
        return entity;
    }

    public void deleteById(final UUID id) {
        client.execute("DELETE type::record('%s', '%s');".formatted(table, id));
    }

    public boolean existsByNombre(final String nombre) {
        var nodes = result(client.execute("SELECT id FROM %s WHERE nombre = '%s' LIMIT 1;"
                .formatted(table, escape(nombre))));
        return nodes.isArray() && !nodes.isEmpty();
    }

    public Optional<T> findById(final UUID id) {
        var nodes = result(client.execute("SELECT * FROM %s:`%s`;".formatted(table, id)));
        return nodes.isArray() && !nodes.isEmpty() ? Optional.of(toEntity(nodes.get(0))) : Optional.empty();
    }

    public List<T> findAll() {
        return query("SELECT * FROM " + table + ";");
    }

    public List<T> findAllPaginado(final int pagina, final int tamanoPagina) {
        var offset = (pagina - 1) * tamanoPagina;
        return query("SELECT * FROM %s LIMIT %d START %d;".formatted(table, tamanoPagina, offset));
    }

    private List<T> query(final String sql) {
        var entities = new ArrayList<T>();
        var nodes = result(client.execute(sql));
        if (nodes.isArray()) {
            nodes.forEach(node -> entities.add(toEntity(node)));
        }
        return entities;
    }

    private T toEntity(final JsonNode node) {
        return factory.apply(extractUuid(node.path("id").asString()), node.path("nombre").asString());
    }

    protected JsonNode result(final JsonNode response) {
        return response.isArray() && !response.isEmpty()
                ? response.get(response.size() - 1).path("result")
                : JsonNodeFactory.instance.arrayNode();
    }

    protected UUID extractUuid(final String recordId) {
        var value = recordId.replace("`", "").replace("u'", "").replace("'", "");
        var separator = value.indexOf(':');
        return UUID.fromString(separator >= 0 ? value.substring(separator + 1) : value);
    }

    private String escape(final String value) {
        return value.replace("\\", "\\\\").replace("'", "\\'");
    }
}
