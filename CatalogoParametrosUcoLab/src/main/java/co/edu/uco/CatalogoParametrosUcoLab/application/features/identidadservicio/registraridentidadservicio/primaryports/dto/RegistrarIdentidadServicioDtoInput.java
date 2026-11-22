package co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.registraridentidadservicio.primaryports.dto;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class RegistrarIdentidadServicioDtoInput {
    private UUID idAmbiente;
    private String clientId;

    private RegistrarIdentidadServicioDtoInput(final UUID idAmbiente, final String clientId) {
        setIdAmbiente(idAmbiente);
        setClientId(clientId);
    }

    public static RegistrarIdentidadServicioDtoInput create(final UUID idAmbiente, final String clientId) {
        return new RegistrarIdentidadServicioDtoInput(idAmbiente, clientId);
    }

    public UUID getIdAmbiente() { return idAmbiente; }
    public void setIdAmbiente(final UUID idAmbiente) { this.idAmbiente = UUIDHelper.getDefault(idAmbiente); }
    public String getClientId() { return clientId; }
    public void setClientId(final String clientId) { this.clientId = TextHelper.applyTrim(clientId); }
}
