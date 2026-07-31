package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.interactor.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.dto.CrearOrganizacionDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.interactor.CrearOrganizacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.interactor.mapper.CrearOrganizacionDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.CrearOrganizacion;

@Service
public class CrearOrganizacionInteractorImpl implements CrearOrganizacionInteractor {

    private static final Logger logger = LoggerFactory.getLogger(CrearOrganizacionInteractorImpl.class);

    private final CrearOrganizacion crearOrganizacion;

    public CrearOrganizacionInteractorImpl(final CrearOrganizacion crearOrganizacion) {
        this.crearOrganizacion = crearOrganizacion;
    }

    @Override
    public void execute(final CrearOrganizacionDto data) {
        logger.info("Iniciando creacion de organizacion: {}", data.getNombre());
        try {
            var organizacionDomain = CrearOrganizacionDtoMapper.INSTANCE.toDomain(data);
            logger.info("Organizacion mapeada a domain: {}", organizacionDomain.getNombre());
            crearOrganizacion.execute(organizacionDomain);
            logger.info("Organizacion creada exitosamente: {}", organizacionDomain.getNombre());
        } catch (final Exception e) {
            logger.error("Error en interactor al crear organizacion: {}", e.getMessage(), e);
            throw e;
        }
    }
}
