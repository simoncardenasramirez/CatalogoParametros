package co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.eliminaridentidadservicio.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.identidadservicio.eliminaridentidadservicio.EliminarIdentidadServicio;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.IdentidadServicioRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class EliminarIdentidadServicioImpl implements EliminarIdentidadServicio {
    private static final Logger LOGGER = LoggerFactory.getLogger(EliminarIdentidadServicioImpl.class);
    private static final String OPERATION_NAME = "eliminar-identidad-servicio";
    private final IdentidadServicioRepository repository;
    private final TelemetryService telemetryService;

    public EliminarIdentidadServicioImpl(final IdentidadServicioRepository repository,
            final TelemetryService telemetryService) {
        this.repository = repository;
        this.telemetryService = telemetryService;
    }

    @Override
    public void execute(final UUID idAplicacion, final String clientId) {
        telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {
            final var identity = repository.findByIdAplicacionAndClientId(idAplicacion, clientId)
                    .orElseThrow(() -> NotFoundException.build("No se encontro la identidad de servicio."));
            repository.deleteById(identity.getId());
            LOGGER.info("[ELIMINAR-IDENTIDAD-SERVICIO] Identidad retirada de aplicacion {}", idAplicacion);
        });
    }
}
