package co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.interactor.parametro.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.dto.parametro.CrearParametroDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.interactor.parametro.CrearParametroInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.mapper.CrearParametroDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.parametro.CrearParametro;

@Service
public class CrearParametroInteractorImpl implements CrearParametroInteractor {

    private final CrearParametro crearParametro;

    public CrearParametroInteractorImpl(final CrearParametro crearParametro) {
        this.crearParametro = crearParametro;
    }

    @Override
    public void execute(final CrearParametroDto data) {
        var parametroDomain = CrearParametroDtoMapper.INSTANCE.toDomain(data);
        crearParametro.execute(parametroDomain);
    }
}
