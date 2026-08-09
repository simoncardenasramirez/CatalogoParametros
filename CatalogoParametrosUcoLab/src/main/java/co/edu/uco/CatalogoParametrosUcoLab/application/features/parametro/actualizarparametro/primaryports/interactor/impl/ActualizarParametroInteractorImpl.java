package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.interactor.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.ActualizarParametro;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.dto.ActualizarParametroDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.dto.ActualizarParametroDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.interactor.ActualizarParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.interactor.mapper.ActualizarParametroDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.ActualizarParametroDomain;

@Service
public final class ActualizarParametroInteractorImpl implements ActualizarParametroInteractor {

    private final ActualizarParametro actualizarParametro;

    public ActualizarParametroInteractorImpl(final ActualizarParametro actualizarParametro) {
        this.actualizarParametro = actualizarParametro;
    }

    @Override
    public void execute(final UUID id, final ActualizarParametroDtoRequest data) {
        final ActualizarParametroDtoInput dtoInput = ActualizarParametroDtoMapper.INSTANCE.toDtoInput(data);
        final ActualizarParametroDomain parametroDomain = ActualizarParametroDtoMapper.INSTANCE.toDomain(id, dtoInput);
        actualizarParametro.execute(parametroDomain);
    }
}
