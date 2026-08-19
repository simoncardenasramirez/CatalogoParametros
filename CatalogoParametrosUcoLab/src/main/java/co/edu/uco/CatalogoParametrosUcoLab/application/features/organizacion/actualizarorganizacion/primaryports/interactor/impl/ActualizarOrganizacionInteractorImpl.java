package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.interactor.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.ActualizarOrganizacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto.ActualizarOrganizacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto.ActualizarOrganizacionDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.interactor.ActualizarOrganizacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.interactor.mapper.ActualizarOrganizacionDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.ActualizarOrganizacionDomain;

@Service
public final class ActualizarOrganizacionInteractorImpl implements ActualizarOrganizacionInteractor {

    private static final Logger logger = LoggerFactory.getLogger(ActualizarOrganizacionInteractorImpl.class);

    private final ActualizarOrganizacion actualizarOrganizacion;

    public ActualizarOrganizacionInteractorImpl(final ActualizarOrganizacion actualizarOrganizacion) {
        this.actualizarOrganizacion = actualizarOrganizacion;
    }

    @Override
    public void execute(final UUID id, final ActualizarOrganizacionDtoRequest data) {
        logger.info("Iniciando actualizacion de organizacion: {}", id);
        try {
            final ActualizarOrganizacionDtoInput dtoInput = ActualizarOrganizacionDtoMapper.INSTANCE.toDtoInput(data);
            logger.info("DTORequest mapeado a DTOInput: {}", dtoInput.getNombre());
            final ActualizarOrganizacionDomain organizacionDomain = ActualizarOrganizacionDtoMapper.INSTANCE.toDomain(id, dtoInput);
            logger.info("DTOInput mapeado a domain: {}", organizacionDomain.getNombre());
            actualizarOrganizacion.execute(organizacionDomain);
            logger.info("Organizacion actualizada exitosamente: {}", organizacionDomain.getNombre());
        } catch (final Exception e) {
            logger.error("Error en interactor al actualizar organizacion: {}", e.getMessage(), e);
            throw e;
        }
    }
}
