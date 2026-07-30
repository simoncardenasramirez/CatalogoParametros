package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.primaryports.interactor.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.EliminarOrganizacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.primaryports.interactor.EliminarOrganizacionInteractor;

@Service
public class EliminarOrganizacionInteractorImpl implements EliminarOrganizacionInteractor {

    private static final Logger logger = LoggerFactory.getLogger(EliminarOrganizacionInteractorImpl.class);

    private final EliminarOrganizacion eliminarOrganizacion;

    public EliminarOrganizacionInteractorImpl(final EliminarOrganizacion eliminarOrganizacion) {
        this.eliminarOrganizacion = eliminarOrganizacion;
    }

    @Override
    public void execute(final java.util.UUID id) {
        logger.info("Iniciando eliminacion de organizacion: {}", id);
        try {
            eliminarOrganizacion.execute(id);
            logger.info("Organizacion eliminada exitosamente: {}", id);
        } catch (final Exception e) {
            logger.error("Error en interactor al eliminar organizacion: {}", e.getMessage(), e);
            throw e;
        }
    }
}
