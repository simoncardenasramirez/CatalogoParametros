package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.actualizarestadotoken.primaryports.interactor.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.actualizarestadotoken.ActualizarEstadoToken;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.actualizarestadotoken.primaryports.dto.ActualizarEstadoTokenDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.actualizarestadotoken.primaryports.interactor.ActualizarEstadoTokenInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.actualizarestadotoken.usecase.domain.ActualizarEstadoTokenDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TokenEntity;

@Service
public final class ActualizarEstadoTokenInteractorImpl implements ActualizarEstadoTokenInteractor {
    private final ActualizarEstadoToken actualizarEstadoToken;

    public ActualizarEstadoTokenInteractorImpl(final ActualizarEstadoToken actualizarEstadoToken) {
        this.actualizarEstadoToken = actualizarEstadoToken;
    }

    @Override
    public TokenEntity execute(final UUID id, final ActualizarEstadoTokenDtoRequest request) {
        return actualizarEstadoToken.execute(
                ActualizarEstadoTokenDomain.create(id, UUID.fromString(request.getEstadoToken())));
    }
}
