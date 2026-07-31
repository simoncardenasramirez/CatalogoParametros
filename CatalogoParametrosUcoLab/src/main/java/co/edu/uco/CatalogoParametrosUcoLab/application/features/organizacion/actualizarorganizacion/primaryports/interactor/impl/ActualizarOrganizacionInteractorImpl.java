package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.interactor.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto.ActualizarOrganizacionDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.interactor.ActualizarOrganizacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.interactor.mapper.ActualizarOrganizacionDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.ActualizarOrganizacion;

@Service
public class ActualizarOrganizacionInteractorImpl implements ActualizarOrganizacionInteractor {

    private static final Logger logger = LoggerFactory.getLogger(ActualizarOrganizacionInteractorImpl.class);

    private final ActualizarOrganizacion actualizarOrganizacion;

    public ActualizarOrganizacionInteractorImpl(final ActualizarOrganizacion actualizarOrganizacion) {
        this.actualizarOrganizacion = actualizarOrganizacion;
    }

    @Override
    public void execute(final ActualizarOrganizacionDto data) {
        logger.info("Iniciando actualizacion de organizacion: {}", data.getId());
        try {
            var organizacionDomain = ActualizarOrganizacionDtoMapper.INSTANCE.toDomain(data);
            logger.info("Organizacion mapeada a domain: {}", organizacionDomain.getNombre());
            actualizarOrganizacion.execute(organizacionDomain);
            logger.info("Organizacion actualizada exitosamente: {}", organizacionDomain.getNombre());
        } catch (final Exception e) {
            logger.error("Error en interactor al actualizar organizacion: {}", e.getMessage(), e);
            throw e;
        }
    }
}
