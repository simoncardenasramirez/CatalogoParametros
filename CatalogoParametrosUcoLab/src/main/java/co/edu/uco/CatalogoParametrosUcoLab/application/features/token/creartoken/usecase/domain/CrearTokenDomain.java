package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.creartoken.usecase.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.Domain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class CrearTokenDomain extends Domain {
    private final String token;
    private final UUID estadoToken;
    private final UUID aplicacionId;
    private final UUID ambienteId;
    private final LocalDateTime fechaExpiracion;

    private CrearTokenDomain(final UUID id, final String token, final UUID estadoToken, final UUID aplicacionId,
            final UUID ambienteId, final LocalDateTime fechaExpiracion) {
        super(id);
        this.token = TextHelper.applyTrim(token);
        this.estadoToken = UUIDHelper.getDefault(estadoToken);
        this.aplicacionId = UUIDHelper.getDefault(aplicacionId);
        this.ambienteId = UUIDHelper.getDefault(ambienteId);
        this.fechaExpiracion = fechaExpiracion;
    }

    public static CrearTokenDomain create(final UUID id, final String token, final UUID estadoToken,
            final UUID aplicacionId, final UUID ambienteId, final LocalDateTime fechaExpiracion) {
        return new CrearTokenDomain(id, token, estadoToken, aplicacionId, ambienteId, fechaExpiracion);
    }

    public String getToken() { return token; }
    public UUID getEstadoToken() { return estadoToken; }
    public UUID getAplicacionId() { return aplicacionId; }
    public UUID getAmbienteId() { return ambienteId; }
    public LocalDateTime getFechaExpiracion() { return fechaExpiracion; }
}
