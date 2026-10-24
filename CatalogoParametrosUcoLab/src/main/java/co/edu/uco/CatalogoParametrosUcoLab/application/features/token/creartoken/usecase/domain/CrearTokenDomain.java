package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.creartoken.usecase.domain;

import java.time.OffsetDateTime;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.DateTimeHelper;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.Domain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class CrearTokenDomain extends Domain {
    private final String token;
    private final UUID estadoToken;
    private final UUID aplicacionId;
    private final UUID ambienteId;
    private final OffsetDateTime fechaExpiracion;

    private CrearTokenDomain(final UUID id, final String token, final UUID estadoToken, final UUID aplicacionId,
            final UUID ambienteId, final OffsetDateTime fechaExpiracion) {
        super(id);
        this.token = TextHelper.applyTrim(token);
        this.estadoToken = UUIDHelper.getDefault(estadoToken);
        this.aplicacionId = UUIDHelper.getDefault(aplicacionId);
        this.ambienteId = UUIDHelper.getDefault(ambienteId);
        this.fechaExpiracion = DateTimeHelper.toUtc(fechaExpiracion);
    }

    public static CrearTokenDomain create(final UUID id, final String token, final UUID estadoToken,
            final UUID aplicacionId, final UUID ambienteId, final OffsetDateTime fechaExpiracion) {
        return new CrearTokenDomain(id, token, estadoToken, aplicacionId, ambienteId, fechaExpiracion);
    }

    public String getToken() { return token; }
    public UUID getEstadoToken() { return estadoToken; }
    public UUID getAplicacionId() { return aplicacionId; }
    public UUID getAmbienteId() { return ambienteId; }
    public OffsetDateTime getFechaExpiracion() { return fechaExpiracion; }
}
