package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametrosporidentidadservicio.primaryports.interactor.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametrosporidentidadservicio.ConsultarParametrosPorIdentidadServicio;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametrosporidentidadservicio.primaryports.interactor.ConsultarParametrosPorIdentidadServicioInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametrosporaplicacionambiente.ConsultarParametrosPorAplicacionAmbiente.Resultado;

@Service
public final class ConsultarParametrosPorIdentidadServicioInteractorImpl
        implements ConsultarParametrosPorIdentidadServicioInteractor {
    private final ConsultarParametrosPorIdentidadServicio consultar;

    public ConsultarParametrosPorIdentidadServicioInteractorImpl(
            final ConsultarParametrosPorIdentidadServicio consultar) {
        this.consultar = consultar;
    }

    @Override
    public Resultado execute(final String clientId) {
        return consultar.execute(clientId);
    }

    @Override
    public Resultado execute(final String clientId, final String nombre) {
        return consultar.execute(clientId, nombre);
    }
}
