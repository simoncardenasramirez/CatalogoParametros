package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.usecase.domain;

import java.util.UUID;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.Domain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.node.JsonNodeFactory;
public final class ActualizarMetadatoDomain extends Domain {
    private UUID idParametro;
    private UUID idTipoMetadato;
    private UUID idAmbiente;
    private JsonNode valor;
    private ActualizarMetadatoDomain(final UUID id, final UUID idParametro, final UUID idTipoMetadato, final UUID idAmbiente, final JsonNode valor) {
        super(id); this.idParametro = UUIDHelper.getDefault(idParametro); this.idTipoMetadato = UUIDHelper.getDefault(idTipoMetadato);
        this.idAmbiente = idAmbiente;
        this.valor = valor == null ? JsonNodeFactory.instance.nullNode() : valor.deepCopy();
    }
    public static ActualizarMetadatoDomain create(final UUID id, final UUID idParametro, final UUID idTipoMetadato, final UUID idAmbiente, final JsonNode valor) {
        return new ActualizarMetadatoDomain(id, idParametro, idTipoMetadato, idAmbiente, valor);
    }
    public static ActualizarMetadatoDomain create(final UUID id, final UUID p, final UUID t, final JsonNode v) { return create(id, p, t, null, v); }
    public UUID getIdAmbiente() { return idAmbiente; }
    public UUID getIdParametro() { return idParametro; }
    public UUID getIdTipoMetadato() { return idTipoMetadato; }
    public JsonNode getValor() { return valor; }
}
