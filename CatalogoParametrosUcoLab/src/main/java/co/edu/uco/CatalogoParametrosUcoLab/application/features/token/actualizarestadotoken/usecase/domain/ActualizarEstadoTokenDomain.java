package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.actualizarestadotoken.usecase.domain;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.Domain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public final class ActualizarEstadoTokenDomain extends Domain {
    private final UUID estadoToken;

    private ActualizarEstadoTokenDomain(final UUID id, final UUID estadoToken) {
        super(id);
        this.estadoToken = UUIDHelper.getDefault(estadoToken);
    }

    public static ActualizarEstadoTokenDomain create(final UUID id, final UUID estadoToken) {
        return new ActualizarEstadoTokenDomain(id, estadoToken);
    }

    public UUID getEstadoToken() { return estadoToken; }
}
