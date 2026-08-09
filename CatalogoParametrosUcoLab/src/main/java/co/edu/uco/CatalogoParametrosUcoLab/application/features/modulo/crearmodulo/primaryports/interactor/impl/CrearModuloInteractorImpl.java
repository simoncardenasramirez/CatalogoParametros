package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.interactor.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.CrearModulo;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.dto.CrearModuloDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.dto.CrearModuloDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.interactor.CrearModuloInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.interactor.mapper.CrearModuloDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.CrearModuloDomain;
import org.springframework.stereotype.Service;

@Service
public final class CrearModuloInteractorImpl implements CrearModuloInteractor {

    private final CrearModulo crearModulo;

    public CrearModuloInteractorImpl(final CrearModulo crearModulo) {
        this.crearModulo = crearModulo;
    }

    @Override
    public void execute(final CrearModuloDtoRequest data) {
        final CrearModuloDtoInput dtoInput = CrearModuloDtoMapper.INSTANCE.toDtoInput(data);
        final CrearModuloDomain moduloDomain = CrearModuloDtoMapper.INSTANCE.toDomain(dtoInput);
        crearModulo.execute(moduloDomain);
    }
}
