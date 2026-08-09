package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.interactor.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.dto.CrearOrganizacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.dto.CrearOrganizacionDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.interactor.CrearOrganizacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.interactor.mapper.CrearOrganizacionDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.CrearOrganizacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.CrearOrganizacionDomain;

@Service
public final class CrearOrganizacionInteractorImpl implements CrearOrganizacionInteractor {

    private static final Logger logger = LoggerFactory.getLogger(CrearOrganizacionInteractorImpl.class);

    private final CrearOrganizacion crearOrganizacion;

    public CrearOrganizacionInteractorImpl(final CrearOrganizacion crearOrganizacion) {
        this.crearOrganizacion = crearOrganizacion;
    }

    @Override
    public void execute(final CrearOrganizacionDtoRequest data) {
        logger.info("Iniciando creacion de organizacion: {}", data.getNombre());
        try {
            final CrearOrganizacionDtoInput dtoInput = CrearOrganizacionDtoMapper.INSTANCE.toDtoInput(data);
            logger.info("DTORequest mapeado a DTOInput: {}", dtoInput.getNombre());
            final CrearOrganizacionDomain organizacionDomain = CrearOrganizacionDtoMapper.INSTANCE.toDomain(dtoInput);
            logger.info("DTOInput mapeado a domain: {}", organizacionDomain.getNombre());
            crearOrganizacion.execute(organizacionDomain);
            logger.info("Organizacion creada exitosamente: {}", organizacionDomain.getNombre());
        } catch (final Exception e) {
            logger.error("Error en interactor al crear organizacion: {}", e.getMessage(), e);
            throw e;
        }
    }
}
