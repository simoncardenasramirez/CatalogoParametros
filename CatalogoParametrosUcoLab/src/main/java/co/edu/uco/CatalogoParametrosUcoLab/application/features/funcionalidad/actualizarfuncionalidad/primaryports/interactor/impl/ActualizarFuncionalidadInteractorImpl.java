package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.interactor.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.ActualizarFuncionalidad;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.dto.ActualizarFuncionalidadDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.interactor.ActualizarFuncionalidadInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.interactor.mapper.ActualizarFuncionalidadDtoMapper;

@Service
public class ActualizarFuncionalidadInteractorImpl implements ActualizarFuncionalidadInteractor {

    private final ActualizarFuncionalidad actualizarFuncionalidad;

    public ActualizarFuncionalidadInteractorImpl(final ActualizarFuncionalidad actualizarFuncionalidad) {
        this.actualizarFuncionalidad = actualizarFuncionalidad;
    }

    @Override
    public void execute(final UUID id, final ActualizarFuncionalidadDto data) {
        var funcionalidadDomain = ActualizarFuncionalidadDtoMapper.INSTANCE.toDomain(id, data);
        actualizarFuncionalidad.execute(funcionalidadDomain);
    }
}