package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import java.time.OffsetDateTime;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.DateTimeHelper;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class TokenEntity {
    private UUID id;
    private String tokenHash;
    private UUID estadoToken;
    private UUID aplicacionId;
    private UUID ambienteId;
    private OffsetDateTime fechaCreacion;
    private OffsetDateTime fechaExpiracion;

    private TokenEntity(final UUID id, final String tokenHash, final UUID estadoToken, final UUID aplicacionId,
            final UUID ambienteId, final OffsetDateTime fechaCreacion, final OffsetDateTime fechaExpiracion) {
        setId(id);
        setTokenHash(tokenHash);
        setEstadoToken(estadoToken);
        setAplicacionId(aplicacionId);
        setAmbienteId(ambienteId);
        setFechaCreacion(fechaCreacion);
        setFechaExpiracion(fechaExpiracion);
    }

    public static TokenEntity create(final UUID id, final String tokenHash, final UUID estadoToken,
            final UUID aplicacionId, final UUID ambienteId, final OffsetDateTime fechaCreacion,
            final OffsetDateTime fechaExpiracion) {
        return new TokenEntity(id, tokenHash, estadoToken, aplicacionId, ambienteId, fechaCreacion, fechaExpiracion);
    }

    public UUID getId() { return id; }
    public void setId(final UUID value) { id = UUIDHelper.getDefault(value); }
    public String getTokenHash() { return tokenHash; }
    public void setTokenHash(final String value) { tokenHash = TextHelper.applyTrim(value); }
    public UUID getEstadoToken() { return estadoToken; }
    public void setEstadoToken(final UUID value) { estadoToken = UUIDHelper.getDefault(value); }
    public UUID getAplicacionId() { return aplicacionId; }
    public void setAplicacionId(final UUID value) { aplicacionId = UUIDHelper.getDefault(value); }
    public UUID getAmbienteId() { return ambienteId; }
    public void setAmbienteId(final UUID value) { ambienteId = UUIDHelper.getDefault(value); }
    public OffsetDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(final OffsetDateTime value) { fechaCreacion = DateTimeHelper.toUtc(value); }
    public OffsetDateTime getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(final OffsetDateTime value) { fechaExpiracion = DateTimeHelper.toUtc(value); }
}
