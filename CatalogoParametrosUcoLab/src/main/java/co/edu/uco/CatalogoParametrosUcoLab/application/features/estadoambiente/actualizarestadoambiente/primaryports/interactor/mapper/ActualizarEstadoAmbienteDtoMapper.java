package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.primaryports.dto.ActualizarEstadoAmbienteDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.primaryports.dto.ActualizarEstadoAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.usecase.domain.ActualizarEstadoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public enum ActualizarEstadoAmbienteDtoMapper {
    INSTANCE;

    public ActualizarEstadoAmbienteDtoInput toDtoInput(final ActualizarEstadoAmbienteDtoRequest dto) {
        final var source = dto == null ? new ActualizarEstadoAmbienteDtoRequest() : dto;
        return ActualizarEstadoAmbienteDtoInput.create(source.getNombre());
    }

    public ActualizarEstadoAmbienteDomain toDomain(final ActualizarEstadoAmbienteDtoInput dto) {
        return ActualizarEstadoAmbienteDomain.create(UUIDHelper.getDefault(), dto.getNombre());
    }

    public ActualizarEstadoAmbienteDomain toDomain(final UUID id, final ActualizarEstadoAmbienteDtoInput dto) {
        return ActualizarEstadoAmbienteDomain.create(id, dto.getNombre());
    }
}
