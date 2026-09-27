package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.primaryports.interactor.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.CrearEstadoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.primaryports.dto.CrearEstadoAmbienteDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.primaryports.dto.CrearEstadoAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.primaryports.interactor.CrearEstadoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.primaryports.interactor.mapper.CrearEstadoAmbienteDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.usecase.domain.CrearEstadoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoAmbienteEntity;

@Service
public final class CrearEstadoAmbienteInteractorImpl implements CrearEstadoAmbienteInteractor {
    private final CrearEstadoAmbiente useCase;

    public CrearEstadoAmbienteInteractorImpl(final CrearEstadoAmbiente useCase) {
        this.useCase = useCase;
    }

    @Override
    public EstadoAmbienteEntity execute(final CrearEstadoAmbienteDtoRequest request) {
        final var mapper = CrearEstadoAmbienteDtoMapper.INSTANCE;
        final CrearEstadoAmbienteDtoInput dtoInput = mapper.toDtoInput(request);
        final CrearEstadoAmbienteDomain domain = mapper.toDomain(dtoInput);
        return useCase.execute(domain);
    }
}
