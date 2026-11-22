package co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.consultaridentidadservicio.primaryports.interactor.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.consultaridentidadservicio.primaryports.interactor.ConsultarIdentidadServicioInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.IdentidadServicioEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.IdentidadServicioRepository;

@Service
public final class ConsultarIdentidadServicioInteractorImpl implements ConsultarIdentidadServicioInteractor {
    private final IdentidadServicioRepository repository;

    public ConsultarIdentidadServicioInteractorImpl(final IdentidadServicioRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<IdentidadServicioEntity> execute(final UUID idAplicacion) {
        return repository.findByIdAplicacion(idAplicacion);
    }
}
