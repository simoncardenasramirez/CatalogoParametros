package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.usecase.domain;

import java.util.UUID;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.Domain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;
public final class ActualizarMetadatoDomain extends Domain {
    private UUID idParametro;
    private UUID idTipoMetadato;
    private String valor;
    private ActualizarMetadatoDomain(final UUID id, final UUID idParametro, final UUID idTipoMetadato, final String valor) {
        super(id); this.idParametro = UUIDHelper.getDefault(idParametro); this.idTipoMetadato = UUIDHelper.getDefault(idTipoMetadato);
        this.valor = TextHelper.applyTrim(valor);
    }
    public static ActualizarMetadatoDomain create(final UUID id, final UUID idParametro, final UUID idTipoMetadato, final String valor) {
        return new ActualizarMetadatoDomain(id, idParametro, idTipoMetadato, valor);
    }
    public UUID getIdParametro() { return idParametro; }
    public UUID getIdTipoMetadato() { return idTipoMetadato; }
    public String getValor() { return valor; }
}
