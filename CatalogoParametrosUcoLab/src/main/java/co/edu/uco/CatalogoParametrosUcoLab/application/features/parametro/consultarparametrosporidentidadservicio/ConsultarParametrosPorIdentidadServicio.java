package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametrosporidentidadservicio;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametrosporaplicacionambiente.ConsultarParametrosPorAplicacionAmbiente.Resultado;

public interface ConsultarParametrosPorIdentidadServicio {
    Resultado execute(String clientId);

    Resultado execute(String clientId, String nombre);
}
