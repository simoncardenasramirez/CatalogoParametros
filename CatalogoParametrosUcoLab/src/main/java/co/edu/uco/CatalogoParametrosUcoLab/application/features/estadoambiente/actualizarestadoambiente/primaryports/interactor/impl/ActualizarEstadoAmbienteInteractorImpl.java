package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.primaryports.interactor.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.ActualizarEstadoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.primaryports.dto.ActualizarEstadoAmbienteDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.primaryports.dto.ActualizarEstadoAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.primaryports.interactor.ActualizarEstadoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.primaryports.interactor.mapper.ActualizarEstadoAmbienteDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.usecase.domain.ActualizarEstadoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoAmbienteEntity;

@Service
public final class ActualizarEstadoAmbienteInteractorImpl implements ActualizarEstadoAmbienteInteractor {
    private final ActualizarEstadoAmbiente useCase;

    public ActualizarEstadoAmbienteInteractorImpl(final ActualizarEstadoAmbiente useCase) {
        this.useCase = useCase;
    }

    @Override
    public EstadoAmbienteEntity execute(final UUID id, final ActualizarEstadoAmbienteDtoRequest request) {
        final var mapper = ActualizarEstadoAmbienteDtoMapper.INSTANCE;
        final ActualizarEstadoAmbienteDtoInput dtoInput = mapper.toDtoInput(request);
        final ActualizarEstadoAmbienteDomain domain = mapper.toDomain(id, dtoInput);
        return useCase.execute(domain);
    }
}
