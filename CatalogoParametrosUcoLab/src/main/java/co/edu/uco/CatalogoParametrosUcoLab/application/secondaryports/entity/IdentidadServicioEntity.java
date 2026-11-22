package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class IdentidadServicioEntity {
    private UUID id;
    private UUID idAplicacion;
    private UUID idAmbiente;
    private String clientId;

    private IdentidadServicioEntity(final UUID id, final UUID idAplicacion, final UUID idAmbiente,
            final String clientId) {
        setId(id);
        setIdAplicacion(idAplicacion);
        setIdAmbiente(idAmbiente);
        setClientId(clientId);
    }

    public static IdentidadServicioEntity create(final UUID id, final UUID idAplicacion, final UUID idAmbiente,
            final String clientId) {
        return new IdentidadServicioEntity(id, idAplicacion, idAmbiente, clientId);
    }

    public UUID getId() { return id; }
    public void setId(final UUID id) { this.id = UUIDHelper.getDefault(id); }
    public UUID getIdAplicacion() { return idAplicacion; }
    public void setIdAplicacion(final UUID idAplicacion) { this.idAplicacion = UUIDHelper.getDefault(idAplicacion); }
    public UUID getIdAmbiente() { return idAmbiente; }
    public void setIdAmbiente(final UUID idAmbiente) { this.idAmbiente = UUIDHelper.getDefault(idAmbiente); }
    public String getClientId() { return clientId; }
    public void setClientId(final String clientId) { this.clientId = TextHelper.applyTrim(clientId); }
}
