package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.interactor.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.ActualizarParametro;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.dto.ActualizarParametroDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.interactor.ActualizarParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.interactor.mapper.ActualizarParametroDtoMapper;

@Service
public class ActualizarParametroInteractorImpl implements ActualizarParametroInteractor {

    private final ActualizarParametro actualizarParametro;

    public ActualizarParametroInteractorImpl(final ActualizarParametro actualizarParametro) {
        this.actualizarParametro = actualizarParametro;
    }

    @Override
    public void execute(final UUID id, final ActualizarParametroDto data) {
        var parametroDomain = ActualizarParametroDtoMapper.INSTANCE.toDomain(id, data);
        actualizarParametro.execute(parametroDomain);
    }
}
