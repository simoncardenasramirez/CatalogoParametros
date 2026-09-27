package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.primaryports.dto.ActualizarMetadatoAmbienteDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.primaryports.dto.ActualizarMetadatoAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.usecase.domain.ActualizarMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public enum ActualizarMetadatoAmbienteDtoMapper {
    INSTANCE;

    public ActualizarMetadatoAmbienteDtoInput toDtoInput(final ActualizarMetadatoAmbienteDtoRequest dto) {
        final var source = dto == null ? new ActualizarMetadatoAmbienteDtoRequest() : dto;
        return ActualizarMetadatoAmbienteDtoInput.create(UUID.fromString(source.getIdParametro()),
                UUID.fromString(source.getIdAmbiente()),
                UUID.fromString(source.getIdEstadoMetadatoAmbiente()));
    }

    public ActualizarMetadatoAmbienteDomain toDomain(final ActualizarMetadatoAmbienteDtoInput dto) {
        return ActualizarMetadatoAmbienteDomain.create(UUIDHelper.getDefault(), dto.getIdParametro(),
                dto.getIdAmbiente(), dto.getIdEstadoMetadatoAmbiente());
    }

    public ActualizarMetadatoAmbienteDomain toDomain(final UUID id, final ActualizarMetadatoAmbienteDtoInput dto) {
        return ActualizarMetadatoAmbienteDomain.create(id, dto.getIdParametro(), dto.getIdAmbiente(),
                dto.getIdEstadoMetadatoAmbiente());
    }
}
