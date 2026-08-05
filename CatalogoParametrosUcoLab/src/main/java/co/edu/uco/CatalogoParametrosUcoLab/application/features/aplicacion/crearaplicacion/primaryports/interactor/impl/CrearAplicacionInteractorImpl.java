package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.interactor.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.dto.CrearAplicacionDto;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.interactor.CrearAplicacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.interactor.mapper.CrearAplicacionDtoMapper;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.CrearAplicacion;

@Service
public class CrearAplicacionInteractorImpl implements CrearAplicacionInteractor {

    private static final Logger logger = LoggerFactory.getLogger(CrearAplicacionInteractorImpl.class);

    private final CrearAplicacion crearAplicacion;
    private final TelemetryService telemetryService;

    public CrearAplicacionInteractorImpl(final CrearAplicacion crearAplicacion,
                                         final TelemetryService telemetryService) {
        this.crearAplicacion = crearAplicacion;
        this.telemetryService = telemetryService;
    }

    @Override
    public void execute(final CrearAplicacionDto data) {
        final var operationName = "crear_aplicacion";
        final var timerSample = telemetryService.startOperationTimer();
        logger.info("Iniciando creacion de aplicacion: {}", data.getNombre());
        try {
            var aplicacionDomain = CrearAplicacionDtoMapper.INSTANCE.toDomain(data);
            logger.info("Aplicacion mapeada a domain: {}", aplicacionDomain.getNombre());
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
