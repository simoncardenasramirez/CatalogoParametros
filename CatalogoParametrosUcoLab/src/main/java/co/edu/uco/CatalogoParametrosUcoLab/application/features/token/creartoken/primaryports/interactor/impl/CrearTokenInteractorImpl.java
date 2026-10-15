package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.creartoken.primaryports.interactor.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.creartoken.CrearToken;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.creartoken.primaryports.dto.CrearTokenDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.creartoken.primaryports.interactor.CrearTokenInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.creartoken.usecase.domain.CrearTokenDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TokenEntity;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class CrearTokenInteractorImpl implements CrearTokenInteractor {
    private final CrearToken crearToken;

    public CrearTokenInteractorImpl(final CrearToken crearToken) { this.crearToken = crearToken; }

    @Override
    public TokenEntity execute(final CrearTokenDtoRequest request) {
        return crearToken.execute(CrearTokenDomain.create(UUIDHelper.getDefault(), request.getToken(),
                UUID.fromString(request.getEstadoToken()), UUID.fromString(request.getAplicacionId()),
                UUID.fromString(request.getAmbienteId()), request.getFechaExpiracion()));
    }
}
