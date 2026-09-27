package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.primaryports.interactor.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.CrearEstadoMetadatoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.primaryports.dto.CrearEstadoMetadatoAmbienteDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.primaryports.dto.CrearEstadoMetadatoAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.primaryports.interactor.CrearEstadoMetadatoAmbienteInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.primaryports.interactor.mapper.CrearEstadoMetadatoAmbienteDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.usecase.domain.CrearEstadoMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;

@Service
public final class CrearEstadoMetadatoAmbienteInteractorImpl implements CrearEstadoMetadatoAmbienteInteractor {
    private final CrearEstadoMetadatoAmbiente useCase;

    public CrearEstadoMetadatoAmbienteInteractorImpl(final CrearEstadoMetadatoAmbiente useCase) {
        this.useCase = useCase;
    }

    @Override
    public EstadoMetadatoAmbienteEntity execute(final CrearEstadoMetadatoAmbienteDtoRequest request) {
        final var mapper = CrearEstadoMetadatoAmbienteDtoMapper.INSTANCE;
        final CrearEstadoMetadatoAmbienteDtoInput dtoInput = mapper.toDtoInput(request);
        final CrearEstadoMetadatoAmbienteDomain domain = mapper.toDomain(dtoInput);
        return useCase.execute(domain);
    }
}
