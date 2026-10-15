package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.validartoken.primaryports.interactor;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.validartoken.primaryports.dto.ValidarTokenDtoRequest;

public interface ValidarTokenInteractor {
    boolean execute(UUID id, ValidarTokenDtoRequest request);
}
