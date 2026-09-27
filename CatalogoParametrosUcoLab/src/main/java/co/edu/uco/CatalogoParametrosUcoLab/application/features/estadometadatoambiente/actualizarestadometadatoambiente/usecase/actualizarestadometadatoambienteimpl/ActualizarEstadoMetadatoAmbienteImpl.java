package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.usecase.actualizarestadometadatoambienteimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.ActualizarEstadoMetadatoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.ActualizarEstadoMetadatoAmbienteRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.secondaryports.event.ActualizarEstadoMetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.secondaryports.publisher.ActualizarEstadoMetadatoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.usecase.domain.ActualizarEstadoMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoMetadatoAmbienteRepository;

@Service
public final class ActualizarEstadoMetadatoAmbienteImpl implements ActualizarEstadoMetadatoAmbiente {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActualizarEstadoMetadatoAmbienteImpl.class);
    private static final String OPERATION_NAME = "actualizar-estado-metadato-ambiente";
    private final EstadoMetadatoAmbienteRepository repository;
    private final ActualizarEstadoMetadatoAmbientePublisher publisher;
    private final TelemetryService telemetryService;
    private final ActualizarEstadoMetadatoAmbienteRuleValidator ruleValidator;

    public ActualizarEstadoMetadatoAmbienteImpl(final EstadoMetadatoAmbienteRepository repository,
            final ActualizarEstadoMetadatoAmbientePublisher publisher, final TelemetryService telemetryService,
            final ActualizarEstadoMetadatoAmbienteRuleValidator ruleValidator) {
        this.repository = repository;
        this.publisher = publisher;
        this.telemetryService = telemetryService;
        this.ruleValidator = ruleValidator;
    }

    @Override
    public EstadoMetadatoAmbienteEntity execute(final ActualizarEstadoMetadatoAmbienteDomain data) {
        return telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {
            LOGGER.info("[ACTUALIZAR-ESTADO-METADATO-AMBIENTE] Iniciando actualizacion con id: {}",
                    data.getId());
            ruleValidator.validate(data);
            final var entity = repository.update(
                    EstadoMetadatoAmbienteEntity.create(data.getId(), data.getNombre()));
            publisher.sendEvent(ActualizarEstadoMetadatoAmbienteEvent.updated(entity));
            LOGGER.info("[ACTUALIZAR-ESTADO-METADATO-AMBIENTE] Estado actualizado exitosamente con id: {}",
                    entity.getId());
            return entity;
        });
    }
}
