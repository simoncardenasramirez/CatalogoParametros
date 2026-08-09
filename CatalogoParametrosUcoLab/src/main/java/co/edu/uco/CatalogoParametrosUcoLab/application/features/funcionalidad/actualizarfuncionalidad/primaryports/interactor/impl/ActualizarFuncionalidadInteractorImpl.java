package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.interactor.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.ActualizarFuncionalidad;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.dto.ActualizarFuncionalidadDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.dto.ActualizarFuncionalidadDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.interactor.ActualizarFuncionalidadInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.interactor.mapper.ActualizarFuncionalidadDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.ActualizarFuncionalidadDomain;

@Service
public final class ActualizarFuncionalidadInteractorImpl implements ActualizarFuncionalidadInteractor {

    private final ActualizarFuncionalidad actualizarFuncionalidad;

    public ActualizarFuncionalidadInteractorImpl(final ActualizarFuncionalidad actualizarFuncionalidad) {
        this.actualizarFuncionalidad = actualizarFuncionalidad;
    }

    @Override
    public void execute(final UUID id, final ActualizarFuncionalidadDtoRequest data) {
        final ActualizarFuncionalidadDtoInput dtoInput = ActualizarFuncionalidadDtoMapper.INSTANCE.toDtoInput(data);
        final ActualizarFuncionalidadDomain funcionalidadDomain = ActualizarFuncionalidadDtoMapper.INSTANCE.toDomain(id, dtoInput);
        actualizarFuncionalidad.execute(funcionalidadDomain);
    }
}
