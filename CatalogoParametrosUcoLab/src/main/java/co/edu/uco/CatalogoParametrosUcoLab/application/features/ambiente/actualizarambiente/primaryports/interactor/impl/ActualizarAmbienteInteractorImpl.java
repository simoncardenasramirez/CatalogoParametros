package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.primaryports.interactor.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.ActualizarAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.primaryports.dto.ActualizarAmbienteDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.primaryports.dto.ActualizarAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.primaryports.interactor.ActualizarAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.primaryports.interactor.mapper.ActualizarAmbienteDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.usecase.domain.ActualizarAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;

@Service
public final class ActualizarAmbienteInteractorImpl implements ActualizarAmbienteInteractor {
    private final ActualizarAmbiente useCase;

    public ActualizarAmbienteInteractorImpl(final ActualizarAmbiente useCase) {
        this.useCase = useCase;
    }

    @Override
    public AmbienteEntity execute(final UUID id, final ActualizarAmbienteDtoRequest request) {
        final var mapper = ActualizarAmbienteDtoMapper.INSTANCE;
        final ActualizarAmbienteDtoInput dtoInput = mapper.toDtoInput(request);
        final ActualizarAmbienteDomain domain = mapper.toDomain(id, dtoInput);
        return useCase.execute(domain);
    }
}
