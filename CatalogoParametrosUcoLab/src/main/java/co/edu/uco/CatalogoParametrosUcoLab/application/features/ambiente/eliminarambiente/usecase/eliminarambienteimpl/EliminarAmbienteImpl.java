package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.eliminarambiente.usecase.eliminarambienteimpl;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.eliminarambiente.EliminarAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.eliminarambiente.secondaryports.event.EliminarAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.eliminarambiente.secondaryports.publisher.EliminarAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class EliminarAmbienteImpl implements EliminarAmbiente {
    private static final Logger LOGGER = LoggerFactory.getLogger(EliminarAmbienteImpl.class);
    private static final String OPERATION_NAME = "eliminar-ambiente";
    private final AmbienteRepository repository;
    private final EliminarAmbientePublisher publisher;
    private final MetadatoAmbienteRepository metadatoRepository;
    private final TelemetryService telemetryService;

    public EliminarAmbienteImpl(final AmbienteRepository repository, final EliminarAmbientePublisher publisher,
            final MetadatoAmbienteRepository metadatoRepository, final TelemetryService telemetryService) {
        this.repository = repository;
        this.publisher = publisher;
        this.metadatoRepository = metadatoRepository;
        this.telemetryService = telemetryService;
    }

    @Override public void execute(final UUID id) {
        telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {
            LOGGER.info("[ELIMINAR-AMBIENTE] Iniciando eliminacion de ambiente con id: {}", id);
            final var entity = repository.findById(id)
                    .orElseThrow(() -> NotFoundException.build("No se encontro ambiente."));
            if (metadatoRepository.existsByIdAmbiente(id)) {
                throw ConflictException.build("No se puede eliminar el ambiente porque tiene metadatos asociados.");
            }
            repository.deleteById(id);
            publisher.sendEvent(EliminarAmbienteEvent.deleted(entity));
            LOGGER.info("[ELIMINAR-AMBIENTE] Ambiente eliminado exitosamente con id: {}", id);
        });
    }
}
