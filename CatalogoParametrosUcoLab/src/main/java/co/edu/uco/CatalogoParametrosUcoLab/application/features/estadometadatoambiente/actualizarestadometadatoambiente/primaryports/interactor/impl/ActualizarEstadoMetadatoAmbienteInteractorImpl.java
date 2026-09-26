package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.primaryports.interactor.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.ActualizarEstadoMetadatoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.primaryports.dto.ActualizarEstadoMetadatoAmbienteDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.primaryports.dto.ActualizarEstadoMetadatoAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.primaryports.interactor.ActualizarEstadoMetadatoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.primaryports.interactor.mapper.ActualizarEstadoMetadatoAmbienteDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.usecase.domain.ActualizarEstadoMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;

@Service
public final class ActualizarEstadoMetadatoAmbienteInteractorImpl implements ActualizarEstadoMetadatoAmbienteInteractor {
    private final ActualizarEstadoMetadatoAmbiente useCase;

    public ActualizarEstadoMetadatoAmbienteInteractorImpl(final ActualizarEstadoMetadatoAmbiente useCase) {
        this.useCase = useCase;
    }

    @Override
    public EstadoMetadatoAmbienteEntity execute(final UUID id, final ActualizarEstadoMetadatoAmbienteDtoRequest request) {
        final var mapper = ActualizarEstadoMetadatoAmbienteDtoMapper.INSTANCE;
        final ActualizarEstadoMetadatoAmbienteDtoInput dtoInput = mapper.toDtoInput(request);
        final ActualizarEstadoMetadatoAmbienteDomain domain = mapper.toDomain(id, dtoInput);
        return useCase.execute(domain);
    }
}
