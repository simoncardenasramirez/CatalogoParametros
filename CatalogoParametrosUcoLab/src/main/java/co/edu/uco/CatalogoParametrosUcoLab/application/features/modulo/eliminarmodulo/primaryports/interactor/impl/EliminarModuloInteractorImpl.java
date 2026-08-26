package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.eliminarmodulo.primaryports.interactor.impl;

import java.util.UUID;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.eliminarmodulo.EliminarModulo;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.eliminarmodulo.primaryports.interactor.EliminarModuloInteractor;
@Service
public final class EliminarModuloInteractorImpl implements EliminarModuloInteractor {
    private final EliminarModulo eliminarModulo;
    public EliminarModuloInteractorImpl(final EliminarModulo eliminarModulo) { this.eliminarModulo = eliminarModulo; }
    @Override public void execute(final UUID id) { eliminarModulo.execute(id); }
}
