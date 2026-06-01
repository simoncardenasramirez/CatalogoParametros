package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.primaryports.interactor.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.EliminarParametro;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.primaryports.interactor.EliminarParametroInteractor;

@Service
public class EliminarParametroInteractorImpl implements EliminarParametroInteractor {

    private final EliminarParametro eliminarParametro;

    public EliminarParametroInteractorImpl(final EliminarParametro eliminarParametro) {
        this.eliminarParametro = eliminarParametro;
    }

    @Override
    public void execute(final UUID data) {
        eliminarParametro.execute(data);
    }
}
