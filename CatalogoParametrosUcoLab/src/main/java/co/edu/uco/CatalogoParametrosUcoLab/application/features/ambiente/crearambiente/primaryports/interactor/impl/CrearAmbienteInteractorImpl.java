package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.primaryports.interactor.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.CrearAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.primaryports.dto.CrearAmbienteDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.primaryports.dto.CrearAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.primaryports.interactor.CrearAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.primaryports.interactor.mapper.CrearAmbienteDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.usecase.domain.CrearAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;

@Service
public final class CrearAmbienteInteractorImpl implements CrearAmbienteInteractor {
    private final CrearAmbiente useCase;

    public CrearAmbienteInteractorImpl(final CrearAmbiente useCase) {
        this.useCase = useCase;
    }

    @Override
    public AmbienteEntity execute(final CrearAmbienteDtoRequest request) {
        final var mapper = CrearAmbienteDtoMapper.INSTANCE;
        final CrearAmbienteDtoInput dtoInput = mapper.toDtoInput(request);
        final CrearAmbienteDomain domain = mapper.toDomain(dtoInput);
        return useCase.execute(domain);
    }
}
