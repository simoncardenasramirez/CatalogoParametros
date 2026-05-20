package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.CrearParametro;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto.CrearParametroDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.mapper.CrearParametroDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.CrearParametroInteractor;
import org.springframework.stereotype.Service;

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
