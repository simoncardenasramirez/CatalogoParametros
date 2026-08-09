package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.interactor.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.dto.CrearFuncionalidadDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.dto.CrearFuncionalidadDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.interactor.CrearFuncionalidadInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.interactor.mapper.CrearFuncionalidadDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.CrearFuncionalidad;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.CrearFuncionalidadDomain;

@Service
public final class CrearFuncionalidadInteractorImpl implements CrearFuncionalidadInteractor {

    private static final Logger logger = LoggerFactory.getLogger(CrearFuncionalidadInteractorImpl.class);

    private final CrearFuncionalidad crearFuncionalidad;

    public CrearFuncionalidadInteractorImpl(final CrearFuncionalidad crearFuncionalidad) {
        this.crearFuncionalidad = crearFuncionalidad;
    }

    @Override
    public void execute(final CrearFuncionalidadDtoRequest data) {
        logger.info("Iniciando creacion de funcionalidad: {}", data.getNombre());
        try {
            final CrearFuncionalidadDtoInput dtoInput = CrearFuncionalidadDtoMapper.INSTANCE.toDtoInput(data);
            logger.info("DTORequest mapeado a DTOInput: {}", dtoInput.getNombre());
            final CrearFuncionalidadDomain funcionalidadDomain = CrearFuncionalidadDtoMapper.INSTANCE.toDomain(dtoInput);
            logger.info("DTOInput mapeado a domain: {}", funcionalidadDomain.getNombre());
            crearFuncionalidad.execute(funcionalidadDomain);
            logger.info("Funcionalidad creada exitosamente: {}", funcionalidadDomain.getNombre());
        } catch (final Exception e) {
            logger.error("Error en interactor al crear funcionalidad: {}", e.getMessage(), e);
            throw e;
        }
    }
}
