package co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.registraridentidadservicio.primaryports.interactor.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.registraridentidadservicio.RegistrarIdentidadServicio;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.registraridentidadservicio.primaryports.dto.RegistrarIdentidadServicioDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.registraridentidadservicio.primaryports.dto.RegistrarIdentidadServicioDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.registraridentidadservicio.primaryports.interactor.RegistrarIdentidadServicioInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.registraridentidadservicio.primaryports.interactor.mapper.RegistrarIdentidadServicioDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.IdentidadServicioEntity;

@Service
public final class RegistrarIdentidadServicioInteractorImpl implements RegistrarIdentidadServicioInteractor {
    private final RegistrarIdentidadServicio useCase;

    public RegistrarIdentidadServicioInteractorImpl(final RegistrarIdentidadServicio useCase) {
        this.useCase = useCase;
    }

    @Override
    public IdentidadServicioEntity execute(final UUID idAplicacion, final RegistrarIdentidadServicioDtoRequest request) {
        final RegistrarIdentidadServicioDtoInput input = RegistrarIdentidadServicioDtoMapper.INSTANCE.toDtoInput(request);
        return useCase.execute(idAplicacion, input);
    }
}
