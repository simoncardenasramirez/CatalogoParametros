package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultartoken.primaryports.interactor;

import java.util.List;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TokenEntity;

public interface ConsultarTokenInteractor {
    List<TokenEntity> execute();
    List<TokenEntity> execute(UUID id);
}
