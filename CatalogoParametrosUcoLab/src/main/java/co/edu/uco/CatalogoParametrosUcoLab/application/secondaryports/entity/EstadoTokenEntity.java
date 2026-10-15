package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class EstadoTokenEntity {
    private UUID id;
    private String estado;

    private EstadoTokenEntity(final UUID id, final String estado) {
        setId(id);
        setEstado(estado);
    }

    public static EstadoTokenEntity create(final UUID id, final String estado) {
        return new EstadoTokenEntity(id, estado);
    }

    public UUID getId() { return id; }
    public void setId(final UUID id) { this.id = UUIDHelper.getDefault(id); }
    public String getEstado() { return estado; }
    public void setEstado(final String estado) { this.estado = TextHelper.applyTrim(estado); }
}
