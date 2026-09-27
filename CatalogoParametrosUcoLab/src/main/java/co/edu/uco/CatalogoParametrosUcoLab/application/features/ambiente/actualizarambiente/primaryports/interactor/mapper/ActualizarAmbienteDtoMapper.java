package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.primaryports.dto.ActualizarAmbienteDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.primaryports.dto.ActualizarAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.usecase.domain.ActualizarAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public enum ActualizarAmbienteDtoMapper {
    INSTANCE;

    public ActualizarAmbienteDtoInput toDtoInput(final ActualizarAmbienteDtoRequest dto) {
        final var source = dto == null ? new ActualizarAmbienteDtoRequest() : dto;
        return ActualizarAmbienteDtoInput.create(source.getNombre());
    }

    public ActualizarAmbienteDomain toDomain(final ActualizarAmbienteDtoInput dto) {
        return ActualizarAmbienteDomain.create(UUIDHelper.getDefault(), dto.getNombre());
    }

    public ActualizarAmbienteDomain toDomain(final UUID id, final ActualizarAmbienteDtoInput dto) {
        return ActualizarAmbienteDomain.create(id, dto.getNombre());
    }
}
