package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.primaryports.interactor.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.EliminarFuncionalidad;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.primaryports.interactor.EliminarFuncionalidadInteractor;

@Service
public class EliminarFuncionalidadInteractorImpl implements EliminarFuncionalidadInteractor {

    private final EliminarFuncionalidad eliminarFuncionalidad;

    public EliminarFuncionalidadInteractorImpl(final EliminarFuncionalidad eliminarFuncionalidad) {
        this.eliminarFuncionalidad = eliminarFuncionalidad;
    }

    @Override
    public void execute(final UUID data) {
        eliminarFuncionalidad.execute(data);
    }
}