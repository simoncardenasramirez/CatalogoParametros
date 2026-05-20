package co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.interactor.modulo.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.dto.modulo.CrearModuloDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.interactor.modulo.CrearModuloInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.mapper.CrearModuloDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.modulo.CrearModulo;

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
