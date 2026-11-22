package co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.registraridentidadservicio.primaryports.dto;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class RegistrarIdentidadServicioDtoRequest {
    private String idAmbiente;
    private String clientId;

    public RegistrarIdentidadServicioDtoRequest() {
        this("", "");
    }

    public RegistrarIdentidadServicioDtoRequest(final String idAmbiente, final String clientId) {
        setIdAmbiente(idAmbiente);
        setClientId(clientId);
    }

    public static RegistrarIdentidadServicioDtoRequest create(final String idAmbiente, final String clientId) {
        return new RegistrarIdentidadServicioDtoRequest(idAmbiente, clientId);
    }

    public String getIdAmbiente() { return idAmbiente; }
    public void setIdAmbiente(final String idAmbiente) { this.idAmbiente = TextHelper.applyTrim(idAmbiente); }
    public String getClientId() { return clientId; }
    public void setClientId(final String clientId) { this.clientId = TextHelper.applyTrim(clientId); }
}
