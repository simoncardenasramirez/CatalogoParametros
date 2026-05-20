package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.interactor.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.CrearModulo;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.dto.CrearModuloDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.interactor.mapper.CrearModuloDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.interactor.CrearModuloInteractor;
import org.springframework.stereotype.Service;

@Service
public class CrearModuloInteractorImpl implements CrearModuloInteractor {

    private final CrearModulo crearModulo;

    public CrearModuloInteractorImpl(final CrearModulo crearModulo) {
        this.crearModulo = crearModulo;
    }

    @Override
    public void execute(final CrearModuloDto data) {
        var moduloDomain = CrearModuloDtoMapper.INSTANCE.toDomain(data);
        crearModulo.execute(moduloDomain);
    }
}
