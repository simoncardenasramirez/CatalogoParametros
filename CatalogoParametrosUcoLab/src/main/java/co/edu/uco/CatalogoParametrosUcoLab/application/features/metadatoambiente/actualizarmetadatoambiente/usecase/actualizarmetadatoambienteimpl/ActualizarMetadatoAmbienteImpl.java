package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.usecase.actualizarmetadatoambienteimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.ActualizarMetadatoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.ActualizarMetadatoAmbienteRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.secondaryports.event.ActualizarMetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.secondaryports.publisher.ActualizarMetadatoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.usecase.domain.ActualizarMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoAmbienteRepository;

@Service
public final class ActualizarMetadatoAmbienteImpl implements ActualizarMetadatoAmbiente {
    private static final Logger LOGGER = LoggerFactory.getLogger(ActualizarMetadatoAmbienteImpl.class);
    private static final String OPERATION_NAME = "actualizar-metadato-ambiente";
    private final MetadatoAmbienteRepository repository;
    private final ActualizarMetadatoAmbientePublisher publisher;
    private final TelemetryService telemetryService;
    private final ActualizarMetadatoAmbienteRuleValidator ruleValidator;

    public ActualizarMetadatoAmbienteImpl(final MetadatoAmbienteRepository repository,
            final ActualizarMetadatoAmbientePublisher publisher, final TelemetryService telemetryService,
            final ActualizarMetadatoAmbienteRuleValidator ruleValidator) {
        this.repository = repository;
        this.publisher = publisher;
        this.telemetryService = telemetryService;
        this.ruleValidator = ruleValidator;
    }

    @Override
    public MetadatoAmbienteEntity execute(final ActualizarMetadatoAmbienteDomain data) {
        return telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {
            LOGGER.info("[ACTUALIZAR-METADATO-AMBIENTE] Iniciando actualizacion con id: {}", data.getId());
            ruleValidator.validate(data);
            final var entity = repository.update(MetadatoAmbienteEntity.create(data.getId(),
                    data.getIdParametro(), data.getIdAmbiente(), data.getIdEstadoMetadatoAmbiente()));
            publisher.sendEvent(ActualizarMetadatoAmbienteEvent.updated(entity));
            LOGGER.info("[ACTUALIZAR-METADATO-AMBIENTE] Metadato actualizado exitosamente con id: {}",
                    entity.getId());
            return entity;
        });
    }

}
