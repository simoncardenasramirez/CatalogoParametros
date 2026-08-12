package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.primaryports.interactor.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.EliminarAplicacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.primaryports.interactor.EliminarAplicacionInteractor;

@Service
public class EliminarAplicacionInteractorImpl implements EliminarAplicacionInteractor {

    private static final Logger logger = LoggerFactory.getLogger(EliminarAplicacionInteractorImpl.class);

    private final EliminarAplicacion eliminarAplicacion;

    public EliminarAplicacionInteractorImpl(final EliminarAplicacion eliminarAplicacion) {
        this.eliminarAplicacion = eliminarAplicacion;
    }

    @Override
    public void execute(final java.util.UUID id) {
        logger.info("Iniciando eliminacion de aplicacion: {}", id);
        try {
            eliminarAplicacion.execute(id);
            logger.info("Aplicacion eliminada exitosamente: {}", id);
        } catch (final Exception e) {
            logger.error("Error en interactor al eliminar aplicacion: {}", e.getMessage(), e);
            throw e;
        }
    }
}
