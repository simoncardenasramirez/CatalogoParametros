package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.primaryports.dto.CrearEstadoMetadatoAmbienteDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.primaryports.dto.CrearEstadoMetadatoAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.usecase.domain.CrearEstadoMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public enum CrearEstadoMetadatoAmbienteDtoMapper {
    INSTANCE;

    public CrearEstadoMetadatoAmbienteDtoInput toDtoInput(final CrearEstadoMetadatoAmbienteDtoRequest dto) {
        final var source = dto == null ? new CrearEstadoMetadatoAmbienteDtoRequest() : dto;
        return CrearEstadoMetadatoAmbienteDtoInput.create(source.getNombre());
    }

    public CrearEstadoMetadatoAmbienteDomain toDomain(final CrearEstadoMetadatoAmbienteDtoInput dto) {
        return CrearEstadoMetadatoAmbienteDomain.create(UUIDHelper.getDefault(), dto.getNombre());
    }

    public CrearEstadoMetadatoAmbienteDomain toDomain(final UUID id, final CrearEstadoMetadatoAmbienteDtoInput dto) {
        return CrearEstadoMetadatoAmbienteDomain.create(id, dto.getNombre());
    }
}
