package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.eliminarestadometadatoambiente.usecase.eliminarestadometadatoambienteimpl;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.eliminarestadometadatoambiente.EliminarEstadoMetadatoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.eliminarestadometadatoambiente.secondaryports.event.EliminarEstadoMetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.eliminarestadometadatoambiente.secondaryports.publisher.EliminarEstadoMetadatoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class EliminarEstadoMetadatoAmbienteImpl implements EliminarEstadoMetadatoAmbiente {
    private static final Logger LOGGER = LoggerFactory.getLogger(EliminarEstadoMetadatoAmbienteImpl.class);
    private static final String OPERATION_NAME = "eliminar-estado-metadato-ambiente";
    private final EstadoMetadatoAmbienteRepository repository;
    private final EliminarEstadoMetadatoAmbientePublisher publisher;
    private final MetadatoAmbienteRepository metadatoRepository;
    private final TelemetryService telemetryService;

    public EliminarEstadoMetadatoAmbienteImpl(final EstadoMetadatoAmbienteRepository repository,
            final EliminarEstadoMetadatoAmbientePublisher publisher,
            final MetadatoAmbienteRepository metadatoRepository, final TelemetryService telemetryService) {
        this.repository = repository;
        this.publisher = publisher;
        this.metadatoRepository = metadatoRepository;
        this.telemetryService = telemetryService;
    }

    @Override public void execute(final UUID id) {
        telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {
            LOGGER.info("[ELIMINAR-ESTADO-METADATO-AMBIENTE] Iniciando eliminacion con id: {}", id);
            final var entity = repository.findById(id)
                    .orElseThrow(() -> NotFoundException.build("No se encontro estadometadatoambiente."));
            if (metadatoRepository.existsByIdEstadoMetadatoAmbiente(id)) {
                throw ConflictException.build("No se puede eliminar el estado porque tiene metadatos asociados.");
            }
            repository.deleteById(id);
            publisher.sendEvent(EliminarEstadoMetadatoAmbienteEvent.deleted(entity));
            LOGGER.info("[ELIMINAR-ESTADO-METADATO-AMBIENTE] Estado eliminado exitosamente con id: {}", id);
        });
    }
}
