package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.CrearParametro;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto.CrearParametroDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto.CrearParametroDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.CrearParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.mapper.CrearParametroDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import org.springframework.stereotype.Service;

@Service
public final class CrearParametroInteractorImpl implements CrearParametroInteractor {

    private final CrearParametro crearParametro;

    public CrearParametroInteractorImpl(final CrearParametro crearParametro) {
        this.crearParametro = crearParametro;
    }

    @Override
    public void execute(final CrearParametroDtoRequest data) {
        final CrearParametroDtoInput dtoInput = CrearParametroDtoMapper.INSTANCE.toDtoInput(data);
        final CrearParametroDomain parametroDomain = CrearParametroDtoMapper.INSTANCE.toDomain(dtoInput);
        crearParametro.execute(parametroDomain);
    }
}
