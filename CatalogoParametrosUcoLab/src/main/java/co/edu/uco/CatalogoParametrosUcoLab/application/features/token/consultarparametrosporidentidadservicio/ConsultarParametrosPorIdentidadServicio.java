package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosporidentidadservicio;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosportoken.ConsultarParametrosPorToken.Resultado;

public interface ConsultarParametrosPorIdentidadServicio {
    Resultado execute(String clientId);
}
