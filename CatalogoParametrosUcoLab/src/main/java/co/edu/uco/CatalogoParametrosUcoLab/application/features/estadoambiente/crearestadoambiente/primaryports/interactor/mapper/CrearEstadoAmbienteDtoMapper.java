package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.primaryports.dto.CrearEstadoAmbienteDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.primaryports.dto.CrearEstadoAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.usecase.domain.CrearEstadoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public enum CrearEstadoAmbienteDtoMapper {
    INSTANCE;

    public CrearEstadoAmbienteDtoInput toDtoInput(final CrearEstadoAmbienteDtoRequest dto) {
        final var source = dto == null ? new CrearEstadoAmbienteDtoRequest() : dto;
        return CrearEstadoAmbienteDtoInput.create(source.getNombre());
    }

    public CrearEstadoAmbienteDomain toDomain(final CrearEstadoAmbienteDtoInput dto) {
        return CrearEstadoAmbienteDomain.create(UUIDHelper.getDefault(), dto.getNombre());
    }

    public CrearEstadoAmbienteDomain toDomain(final UUID id, final CrearEstadoAmbienteDtoInput dto) {
        return CrearEstadoAmbienteDomain.create(id, dto.getNombre());
    }
}
