package co.edu.uco.CatalogoParametrosUcoLab.domain.metadato;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;
import co.edu.uco.CatalogoParametrosUcoLab.domain.Domain;

public final class MetadatoDomain extends Domain {

    private UUID idParametro;
    private UUID idTipoMetadato;
    private String valor;

    private MetadatoDomain(final UUID id, final UUID idParametro, final UUID idTipoMetadato, final String valor) {
        super(id);
        setIdParametro(idParametro);
        setIdTipoMetadato(idTipoMetadato);
        setValor(valor);
    }

    public static MetadatoDomain create(final UUID id, final UUID idParametro, final UUID idTipoMetadato, final String valor) {
        return new MetadatoDomain(id, idParametro, idTipoMetadato, valor);
    }

    public UUID getIdParametro() {
        return idParametro;
    }

    private void setIdParametro(final UUID idParametro) {
        this.idParametro = UUIDHelper.getDefault(idParametro);
    }

    public UUID getIdTipoMetadato() {
        return idTipoMetadato;
    }

    private void setIdTipoMetadato(final UUID idTipoMetadato) {
        this.idTipoMetadato = UUIDHelper.getDefault(idTipoMetadato);
    }

    public String getValor() {
        return valor;
    }

    private void setValor(final String valor) {
        this.valor = TextHelper.applyTrim(valor);
    }
}
