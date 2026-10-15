package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.creartoken.primaryports.dto;

import java.time.LocalDateTime;

public final class CrearTokenDtoRequest {
    private String token;
    private String estadoToken;
    private String aplicacionId;
    private String ambienteId;
    private LocalDateTime fechaExpiracion;

    public CrearTokenDtoRequest() { }

    public String getToken() { return token; }
    public void setToken(final String value) { token = value; }
    public String getEstadoToken() { return estadoToken; }
    public void setEstadoToken(final String value) { estadoToken = value; }
    public String getAplicacionId() { return aplicacionId; }
    public void setAplicacionId(final String value) { aplicacionId = value; }
    public String getAmbienteId() { return ambienteId; }
    public void setAmbienteId(final String value) { ambienteId = value; }
    public LocalDateTime getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(final LocalDateTime value) { fechaExpiracion = value; }
}
