package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.validartoken.primaryports.interactor.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.validartoken.ValidarToken;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.validartoken.primaryports.dto.ValidarTokenDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.validartoken.primaryports.interactor.ValidarTokenInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.validartoken.usecase.domain.ValidarTokenDomain;

@Service
public final class ValidarTokenInteractorImpl implements ValidarTokenInteractor {
    private final ValidarToken validarToken;

    public ValidarTokenInteractorImpl(final ValidarToken validarToken) {
        this.validarToken = validarToken;
    }

    @Override
    public boolean execute(final UUID id, final ValidarTokenDtoRequest request) {
        return validarToken.execute(ValidarTokenDomain.create(id, request.getToken()));
    }
}
