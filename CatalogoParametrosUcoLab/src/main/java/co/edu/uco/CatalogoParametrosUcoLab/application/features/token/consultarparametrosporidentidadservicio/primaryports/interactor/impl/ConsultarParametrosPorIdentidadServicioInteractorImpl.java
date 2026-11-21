package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosporidentidadservicio.primaryports.interactor.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosporidentidadservicio.ConsultarParametrosPorIdentidadServicio;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosporidentidadservicio.primaryports.interactor.ConsultarParametrosPorIdentidadServicioInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosportoken.ConsultarParametrosPorToken.Resultado;

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
}
