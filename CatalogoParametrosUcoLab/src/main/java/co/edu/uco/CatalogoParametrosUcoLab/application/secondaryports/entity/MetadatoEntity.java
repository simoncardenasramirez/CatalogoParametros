package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.JsonNodeFactory;

public final class MetadatoEntity {
    private UUID id;
    private UUID idParametro;
    private UUID idTipoMetadato;
    private JsonNode valor;

    private MetadatoEntity(final UUID id, final UUID idParametro, final UUID idTipoMetadato, final JsonNode valor) {
        setId(id);
        setIdParametro(idParametro);
        setIdTipoMetadato(idTipoMetadato);
        setValor(valor);
    }

    public static MetadatoEntity create(final UUID id, final UUID idParametro, final UUID idTipoMetadato,
            final JsonNode valor) {
        return new MetadatoEntity(id, idParametro, idTipoMetadato, valor);
    }

    public UUID getId() { return id; }
    public void setId(final UUID id) { this.id = UUIDHelper.getDefault(id); }
    public UUID getIdParametro() { return idParametro; }
    public void setIdParametro(final UUID idParametro) { this.idParametro = UUIDHelper.getDefault(idParametro); }
    public UUID getIdTipoMetadato() { return idTipoMetadato; }
    public void setIdTipoMetadato(final UUID idTipoMetadato) { this.idTipoMetadato = UUIDHelper.getDefault(idTipoMetadato); }
    public JsonNode getValor() { return valor; }
    public void setValor(final JsonNode valor) {
        this.valor = valor == null ? JsonNodeFactory.instance.nullNode() : valor.deepCopy();
    }
}
