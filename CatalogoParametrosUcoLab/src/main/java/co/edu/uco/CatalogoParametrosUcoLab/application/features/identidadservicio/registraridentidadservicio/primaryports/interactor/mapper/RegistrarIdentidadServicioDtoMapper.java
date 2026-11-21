package co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.registraridentidadservicio.primaryports.interactor.mapper;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.registraridentidadservicio.primaryports.dto.RegistrarIdentidadServicioDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.registraridentidadservicio.primaryports.dto.RegistrarIdentidadServicioDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

public enum RegistrarIdentidadServicioDtoMapper {
    INSTANCE;

    public RegistrarIdentidadServicioDtoInput toDtoInput(final RegistrarIdentidadServicioDtoRequest request) {
        final var source = request == null ? new RegistrarIdentidadServicioDtoRequest() : request;
        try {
            return RegistrarIdentidadServicioDtoInput.create(UUID.fromString(source.getIdAmbiente()), source.getClientId());
        } catch (final IllegalArgumentException exception) {
            throw ValidationException.build("El identificador del ambiente no es valido.");
        }
    }
}
