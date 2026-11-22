package co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.eliminaridentidadservicio.primaryports.interactor.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.eliminaridentidadservicio.primaryports.interactor.EliminarIdentidadServicioInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.eliminaridentidadservicio.EliminarIdentidadServicio;

@Service
public final class EliminarIdentidadServicioInteractorImpl implements EliminarIdentidadServicioInteractor {
    private final EliminarIdentidadServicio useCase;

    public EliminarIdentidadServicioInteractorImpl(final EliminarIdentidadServicio useCase) {
        this.useCase = useCase;
    }

    @Override
    public void execute(final UUID idAplicacion, final String clientId) {
        useCase.execute(idAplicacion, clientId);
    }
}
