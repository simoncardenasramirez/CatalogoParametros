package co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.interactor.funcionalidad.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.dto.funcionalidad.CrearFuncionalidadDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.interactor.funcionalidad.CrearFuncionalidadInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.mapper.CrearFuncionalidadDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.funcionalidad.CrearFuncionalidad;

@Service
public class CrearFuncionalidadInteractorImpl implements CrearFuncionalidadInteractor {

    private static final Logger logger = LoggerFactory.getLogger(CrearFuncionalidadInteractorImpl.class);

    private final CrearFuncionalidad crearFuncionalidad;

    public CrearFuncionalidadInteractorImpl(final CrearFuncionalidad crearFuncionalidad) {
        this.crearFuncionalidad = crearFuncionalidad;
    }

    @Override
    public void execute(final CrearFuncionalidadDto data) {
        logger.info("Iniciando creacion de funcionalidad: {}", data.getNombre());
        try {
            var funcionalidadDomain = CrearFuncionalidadDtoMapper.INSTANCE.toDomain(data);
            logger.info("Funcionalidad mapeada a domain: {}", funcionalidadDomain.getNombre());
            crearFuncionalidad.execute(funcionalidadDomain);
            logger.info("Funcionalidad creada exitosamente: {}", funcionalidadDomain.getNombre());
        } catch (final Exception e) {
            logger.error("Error en interactor al crear funcionalidad: {}", e.getMessage(), e);
            throw e;
        }
    }
}