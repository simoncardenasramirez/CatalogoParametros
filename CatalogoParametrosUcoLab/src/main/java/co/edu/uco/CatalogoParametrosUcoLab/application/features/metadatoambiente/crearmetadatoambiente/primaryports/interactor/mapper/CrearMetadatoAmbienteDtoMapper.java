package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.primaryports.dto.CrearMetadatoAmbienteDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.primaryports.dto.CrearMetadatoAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.domain.CrearMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public enum CrearMetadatoAmbienteDtoMapper {
    INSTANCE;

    public CrearMetadatoAmbienteDtoInput toDtoInput(final CrearMetadatoAmbienteDtoRequest dto) {
        final var source = dto == null ? new CrearMetadatoAmbienteDtoRequest() : dto;
        return CrearMetadatoAmbienteDtoInput.create(UUID.fromString(source.getIdParametro()),
                UUID.fromString(source.getIdAmbiente()),
                UUID.fromString(source.getIdEstadoMetadatoAmbiente()));
    }

    public CrearMetadatoAmbienteDomain toDomain(final CrearMetadatoAmbienteDtoInput dto) {
        return CrearMetadatoAmbienteDomain.create(UUIDHelper.getDefault(), dto.getIdParametro(),
                dto.getIdAmbiente(), dto.getIdEstadoMetadatoAmbiente());
    }

    public CrearMetadatoAmbienteDomain toDomain(final UUID id, final CrearMetadatoAmbienteDtoInput dto) {
        return CrearMetadatoAmbienteDomain.create(id, dto.getIdParametro(), dto.getIdAmbiente(),
                dto.getIdEstadoMetadatoAmbiente());
    }
}
