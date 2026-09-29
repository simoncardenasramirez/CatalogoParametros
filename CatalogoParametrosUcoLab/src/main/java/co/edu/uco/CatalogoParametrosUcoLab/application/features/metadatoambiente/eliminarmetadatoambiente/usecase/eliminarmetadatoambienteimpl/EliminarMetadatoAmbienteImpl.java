package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.eliminarmetadatoambiente.usecase.eliminarmetadatoambienteimpl;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.eliminarmetadatoambiente.EliminarMetadatoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.eliminarmetadatoambiente.secondaryports.event.EliminarMetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.eliminarmetadatoambiente.secondaryports.publisher.EliminarMetadatoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class EliminarMetadatoAmbienteImpl implements EliminarMetadatoAmbiente {
    private static final Logger LOGGER = LoggerFactory.getLogger(EliminarMetadatoAmbienteImpl.class);
    private static final String OPERATION_NAME = "eliminar-metadato-ambiente";
    private final MetadatoAmbienteRepository repository;
    private final EliminarMetadatoAmbientePublisher publisher;
    private final TelemetryService telemetryService;

    public EliminarMetadatoAmbienteImpl(final MetadatoAmbienteRepository repository,
            final EliminarMetadatoAmbientePublisher publisher, final TelemetryService telemetryService) {
        this.repository = repository;
        this.publisher = publisher;
        this.telemetryService = telemetryService;
    }

    @Override public void execute(final UUID id) {
        telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {
            LOGGER.info("[ELIMINAR-METADATO-AMBIENTE] Iniciando eliminacion con id: {}", id);
            final var entity = repository.findById(id)
                    .orElseThrow(() -> NotFoundException.build("No se encontro metadatoambiente."));
            repository.deleteById(id);
            publisher.sendEvent(EliminarMetadatoAmbienteEvent.deleted(entity));
            LOGGER.info("[ELIMINAR-METADATO-AMBIENTE] Metadato eliminado exitosamente con id: {}", id);
        });
    }
}
