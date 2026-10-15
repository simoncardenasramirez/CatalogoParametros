package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.actualizarestadotoken.primaryports.interactor;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.actualizarestadotoken.primaryports.dto.ActualizarEstadoTokenDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TokenEntity;

public interface ActualizarEstadoTokenInteractor {
    TokenEntity execute(UUID id, ActualizarEstadoTokenDtoRequest request);
}
