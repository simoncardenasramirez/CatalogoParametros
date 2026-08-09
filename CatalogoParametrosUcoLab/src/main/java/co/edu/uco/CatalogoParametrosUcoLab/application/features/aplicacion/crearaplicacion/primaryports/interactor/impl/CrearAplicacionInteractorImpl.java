package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.interactor.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.dto.CrearAplicacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.dto.CrearAplicacionDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.interactor.CrearAplicacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.interactor.mapper.CrearAplicacionDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.CrearAplicacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.CrearAplicacionDomain;

@Service
public final class CrearAplicacionInteractorImpl implements CrearAplicacionInteractor {

    private static final Logger logger = LoggerFactory.getLogger(CrearAplicacionInteractorImpl.class);

    private final CrearAplicacion crearAplicacion;
    private final TelemetryService telemetryService;

    public CrearAplicacionInteractorImpl(final CrearAplicacion crearAplicacion,
                                         final TelemetryService telemetryService) {
        this.crearAplicacion = crearAplicacion;
        this.telemetryService = telemetryService;
    }

    @Override
    public void execute(final CrearAplicacionDtoRequest data) {
        final var operationName = "crear_aplicacion";
        final var timerSample = telemetryService.startOperationTimer();
        logger.info("Iniciando creacion de aplicacion: {}", data.getNombre());
        try {
            final CrearAplicacionDtoInput dtoInput = CrearAplicacionDtoMapper.INSTANCE.toDtoInput(data);
            logger.info("DTORequest mapeado a DTOInput: {}", dtoInput.getNombre());
            final CrearAplicacionDomain aplicacionDomain = CrearAplicacionDtoMapper.INSTANCE.toDomain(dtoInput);
            logger.info("DTOInput mapeado a domain: {}", aplicacionDomain.getNombre());
            crearAplicacion.execute(aplicacionDomain);
            telemetryService.recordBusinessOperation(operationName);
            telemetryService.stopOperationTimer(timerSample, operationName);
            logger.info("Aplicacion creada exitosamente: {}", aplicacionDomain.getNombre());
        } catch (final Exception e) {
            telemetryService.recordBusinessError(operationName, e);
            telemetryService.stopOperationTimer(timerSample, operationName + ".error");
            logger.error("Error en interactor al crear aplicacion: {}", e.getMessage(), e);
            throw e;
        }
    }
}
