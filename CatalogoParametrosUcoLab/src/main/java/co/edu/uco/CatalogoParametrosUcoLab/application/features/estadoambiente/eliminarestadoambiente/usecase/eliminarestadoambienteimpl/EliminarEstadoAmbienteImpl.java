package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.eliminarestadoambiente.usecase.eliminarestadoambienteimpl;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.eliminarestadoambiente.EliminarEstadoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.eliminarestadoambiente.secondaryports.event.EliminarEstadoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.eliminarestadoambiente.secondaryports.publisher.EliminarEstadoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class EliminarEstadoAmbienteImpl implements EliminarEstadoAmbiente {
    private static final Logger LOGGER = LoggerFactory.getLogger(EliminarEstadoAmbienteImpl.class);
    private static final String OPERATION_NAME = "eliminar-estado-ambiente";
    private final EstadoAmbienteRepository repository;
    private final EliminarEstadoAmbientePublisher publisher;
    private final TelemetryService telemetryService;

    public EliminarEstadoAmbienteImpl(final EstadoAmbienteRepository repository,
            final EliminarEstadoAmbientePublisher publisher, final TelemetryService telemetryService) {
        this.repository = repository;
        this.publisher = publisher;
        this.telemetryService = telemetryService;
    }

    @Override public void execute(final UUID id) {
        telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {
            LOGGER.info("[ELIMINAR-ESTADO-AMBIENTE] Iniciando eliminacion con id: {}", id);
            final var entity = repository.findById(id)
                    .orElseThrow(() -> NotFoundException.build("No se encontro estadoambiente."));
            repository.deleteById(id);
            publisher.sendEvent(EliminarEstadoAmbienteEvent.deleted(entity));
            LOGGER.info("[ELIMINAR-ESTADO-AMBIENTE] Estado eliminado exitosamente con id: {}", id);
        });
    }
}
