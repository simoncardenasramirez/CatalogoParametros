package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.creartoken.primaryports.interactor;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.creartoken.primaryports.dto.CrearTokenDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TokenEntity;

public interface CrearTokenInteractor {
    TokenEntity execute(CrearTokenDtoRequest request);
}
