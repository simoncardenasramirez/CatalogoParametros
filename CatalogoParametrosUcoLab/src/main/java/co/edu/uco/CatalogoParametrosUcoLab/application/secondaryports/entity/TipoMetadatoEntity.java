package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class TipoMetadatoEntity {
    private UUID id;
    private String tipo;
    private String detalle;

    private TipoMetadatoEntity(final UUID id, final String tipo, final String detalle) {
        setId(id);
        setTipo(tipo);
        setDetalle(detalle);
    }

    public static TipoMetadatoEntity create(final UUID id, final String tipo, final String detalle) {
        return new TipoMetadatoEntity(id, tipo, detalle);
    }

    public UUID getId() { return id; }
    public void setId(final UUID id) { this.id = UUIDHelper.getDefault(id); }
    public String getTipo() { return tipo; }
    public void setTipo(final String tipo) { this.tipo = TextHelper.applyTrim(tipo); }
    public String getDetalle() { return detalle; }
    public void setDetalle(final String detalle) { this.detalle = TextHelper.applyTrim(detalle); }
}
