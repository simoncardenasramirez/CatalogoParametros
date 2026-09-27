package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.usecase.crearestadometadatoambienteimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.CrearEstadoMetadatoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.CrearEstadoMetadatoAmbienteRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.secondaryports.event.CrearEstadoMetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.secondaryports.publisher.CrearEstadoMetadatoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.usecase.domain.CrearEstadoMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class CrearEstadoMetadatoAmbienteImpl implements CrearEstadoMetadatoAmbiente {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrearEstadoMetadatoAmbienteImpl.class);
    private static final String OPERATION_NAME = "crear-estado-metadato-ambiente";
    private final EstadoMetadatoAmbienteRepository repository;
    private final CrearEstadoMetadatoAmbientePublisher publisher;
    private final TelemetryService telemetryService;
    private final CrearEstadoMetadatoAmbienteRuleValidator ruleValidator;

    public CrearEstadoMetadatoAmbienteImpl(final EstadoMetadatoAmbienteRepository repository,
            final CrearEstadoMetadatoAmbientePublisher publisher, final TelemetryService telemetryService,
            final CrearEstadoMetadatoAmbienteRuleValidator ruleValidator) {
        this.repository = repository;
        this.publisher = publisher;
        this.telemetryService = telemetryService;
        this.ruleValidator = ruleValidator;
    }

    @Override
    public EstadoMetadatoAmbienteEntity execute(final CrearEstadoMetadatoAmbienteDomain data) {
        return telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {
            LOGGER.info("[CREAR-ESTADO-METADATO-AMBIENTE] Iniciando creacion: {}", data.getNombre());
            ruleValidator.validate(data);
            final var entity = repository.save(
                    EstadoMetadatoAmbienteEntity.create(UUIDHelper.generate(), data.getNombre()));
            publisher.sendEvent(CrearEstadoMetadatoAmbienteEvent.created(entity));
            LOGGER.info("[CREAR-ESTADO-METADATO-AMBIENTE] Estado creado exitosamente con id: {}",
                    entity.getId());
            return entity;
        });
    }
}
