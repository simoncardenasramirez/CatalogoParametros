package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.primaryports.dto.CrearAmbienteDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.primaryports.dto.CrearAmbienteDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.usecase.domain.CrearAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

public enum CrearAmbienteDtoMapper {
    INSTANCE;

    public CrearAmbienteDtoInput toDtoInput(final CrearAmbienteDtoRequest dto) {
        final var source = dto == null ? new CrearAmbienteDtoRequest() : dto;
        return CrearAmbienteDtoInput.create(source.getNombre());
    }

    public CrearAmbienteDomain toDomain(final CrearAmbienteDtoInput dto) {
        return CrearAmbienteDomain.create(UUIDHelper.getDefault(), dto.getNombre());
    }

    public CrearAmbienteDomain toDomain(final UUID id, final CrearAmbienteDtoInput dto) {
        return CrearAmbienteDomain.create(id, dto.getNombre());
    }
}
