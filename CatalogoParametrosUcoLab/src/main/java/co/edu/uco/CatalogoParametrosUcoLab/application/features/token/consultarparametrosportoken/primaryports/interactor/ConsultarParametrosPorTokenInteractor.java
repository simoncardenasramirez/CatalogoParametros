package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosportoken.primaryports.interactor;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosportoken.ConsultarParametrosPorToken.Resultado;

public interface ConsultarParametrosPorTokenInteractor {
    Resultado execute(UUID tokenId, String authorization);
}
