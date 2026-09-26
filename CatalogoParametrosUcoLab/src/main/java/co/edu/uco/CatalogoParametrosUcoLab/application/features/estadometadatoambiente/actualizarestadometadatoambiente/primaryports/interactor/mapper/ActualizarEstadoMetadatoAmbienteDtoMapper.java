package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.primaryports.dto.ActualizarEstadoMetadatoAmbienteDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.primaryports.dto.ActualizarEstadoMetadatoAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.usecase.domain.ActualizarEstadoMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public enum ActualizarEstadoMetadatoAmbienteDtoMapper {
    INSTANCE;

    public ActualizarEstadoMetadatoAmbienteDtoInput toDtoInput(final ActualizarEstadoMetadatoAmbienteDtoRequest dto) {
        final var source = dto == null ? new ActualizarEstadoMetadatoAmbienteDtoRequest() : dto;
        return ActualizarEstadoMetadatoAmbienteDtoInput.create(source.getNombre());
    }

    public ActualizarEstadoMetadatoAmbienteDomain toDomain(final ActualizarEstadoMetadatoAmbienteDtoInput dto) {
        return ActualizarEstadoMetadatoAmbienteDomain.create(UUIDHelper.getDefault(), dto.getNombre());
    }

    public ActualizarEstadoMetadatoAmbienteDomain toDomain(final UUID id, final ActualizarEstadoMetadatoAmbienteDtoInput dto) {
        return ActualizarEstadoMetadatoAmbienteDomain.create(id, dto.getNombre());
    }
}
