package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametrosporidentidadservicio.primaryports.interactor;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametrosporaplicacionambiente.ConsultarParametrosPorAplicacionAmbiente.Resultado;

public interface ConsultarParametrosPorIdentidadServicioInteractor {
    Resultado execute(String clientId);

    Resultado execute(String clientId, String nombre);
}
