package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.primaryports.interactor.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.ActualizarModulo;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.primaryports.dto.ActualizarModuloDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.primaryports.dto.ActualizarModuloDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.primaryports.interactor.ActualizarModuloInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.primaryports.interactor.mapper.ActualizarModuloDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.ActualizarModuloDomain;

@Service
public final class ActualizarModuloInteractorImpl implements ActualizarModuloInteractor {

    private final ActualizarModulo actualizarModulo;

    public ActualizarModuloInteractorImpl(final ActualizarModulo actualizarModulo) {
        this.actualizarModulo = actualizarModulo;
    }

    @Override
    public void execute(final UUID id, final ActualizarModuloDtoRequest data) {
        final ActualizarModuloDtoInput dtoInput = ActualizarModuloDtoMapper.INSTANCE.toDtoInput(data);
        final ActualizarModuloDomain moduloDomain = ActualizarModuloDtoMapper.INSTANCE.toDomain(id, dtoInput);
        actualizarModulo.execute(moduloDomain);
    }
}
