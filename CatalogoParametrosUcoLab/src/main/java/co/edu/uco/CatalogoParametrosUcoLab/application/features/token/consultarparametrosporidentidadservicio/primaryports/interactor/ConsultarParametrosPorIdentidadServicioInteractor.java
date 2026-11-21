package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosporidentidadservicio.primaryports.interactor;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosporidentidadservicio.ConsultarParametrosPorIdentidadServicio;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosportoken.ConsultarParametrosPorToken.Resultado;

public interface ConsultarParametrosPorIdentidadServicioInteractor {
    Resultado execute(String clientId);
}
