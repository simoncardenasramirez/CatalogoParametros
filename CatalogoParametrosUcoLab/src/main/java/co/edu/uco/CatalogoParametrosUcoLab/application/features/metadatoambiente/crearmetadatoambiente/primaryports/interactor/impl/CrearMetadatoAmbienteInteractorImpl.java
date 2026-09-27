package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.primaryports.interactor.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.CrearMetadatoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.primaryports.dto.CrearMetadatoAmbienteDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.primaryports.dto.CrearMetadatoAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.primaryports.interactor.CrearMetadatoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.primaryports.interactor.mapper.CrearMetadatoAmbienteDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.domain.CrearMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;

@Service
public final class CrearMetadatoAmbienteInteractorImpl implements CrearMetadatoAmbienteInteractor {
    private final CrearMetadatoAmbiente useCase;

    public CrearMetadatoAmbienteInteractorImpl(final CrearMetadatoAmbiente useCase) {
        this.useCase = useCase;
    }

    @Override
    public MetadatoAmbienteEntity execute(final CrearMetadatoAmbienteDtoRequest request) {
        final var mapper = CrearMetadatoAmbienteDtoMapper.INSTANCE;
        final CrearMetadatoAmbienteDtoInput dtoInput = mapper.toDtoInput(request);
        final CrearMetadatoAmbienteDomain domain = mapper.toDomain(dtoInput);
        return useCase.execute(domain);
    }
}
