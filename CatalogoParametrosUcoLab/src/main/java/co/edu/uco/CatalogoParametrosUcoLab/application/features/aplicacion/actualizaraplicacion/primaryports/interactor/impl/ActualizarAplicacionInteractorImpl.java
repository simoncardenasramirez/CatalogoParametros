package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.primaryports.interactor.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.ActualizarAplicacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.primaryports.dto.ActualizarAplicacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.primaryports.dto.ActualizarAplicacionDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.primaryports.interactor.ActualizarAplicacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.primaryports.interactor.mapper.ActualizarAplicacionDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.ActualizarAplicacionDomain;

@Service
public final class ActualizarAplicacionInteractorImpl implements ActualizarAplicacionInteractor {

    private final ActualizarAplicacion actualizarAplicacion;

    public ActualizarAplicacionInteractorImpl(final ActualizarAplicacion actualizarAplicacion) {
        this.actualizarAplicacion = actualizarAplicacion;
    }

    @Override
    public void execute(final UUID id, final ActualizarAplicacionDtoRequest data) {
        final ActualizarAplicacionDtoInput dtoInput = ActualizarAplicacionDtoMapper.INSTANCE.toDtoInput(data);
        final ActualizarAplicacionDomain aplicacionDomain = ActualizarAplicacionDtoMapper.INSTANCE.toDomain(id, dtoInput);
        actualizarAplicacion.execute(aplicacionDomain);
    }
}
