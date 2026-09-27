package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.primaryports.interactor.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.ActualizarMetadatoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.primaryports.dto.ActualizarMetadatoAmbienteDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.primaryports.dto.ActualizarMetadatoAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.primaryports.interactor.ActualizarMetadatoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.primaryports.interactor.mapper.ActualizarMetadatoAmbienteDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.usecase.domain.ActualizarMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;

@Service
public final class ActualizarMetadatoAmbienteInteractorImpl implements ActualizarMetadatoAmbienteInteractor {
    private final ActualizarMetadatoAmbiente useCase;

    public ActualizarMetadatoAmbienteInteractorImpl(final ActualizarMetadatoAmbiente useCase) {
        this.useCase = useCase;
    }

    @Override
    public MetadatoAmbienteEntity execute(final UUID id, final ActualizarMetadatoAmbienteDtoRequest request) {
        final var mapper = ActualizarMetadatoAmbienteDtoMapper.INSTANCE;
        final ActualizarMetadatoAmbienteDtoInput dtoInput = mapper.toDtoInput(request);
        final ActualizarMetadatoAmbienteDomain domain = mapper.toDomain(id, dtoInput);
        return useCase.execute(domain);
    }
}
