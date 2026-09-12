package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.usecase.actualizarmetadatoimpl;

import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.*;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.secondaryports.event.ActualizarMetadatoEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.secondaryports.publisher.ActualizarMetadatoPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.usecase.domain.ActualizarMetadatoDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoRepository;
@Service
public final class ActualizarMetadatoImpl implements ActualizarMetadato {
    private final MetadatoRepository repository; 
    private final ActualizarMetadatoRuleValidator validator;
    private final ActualizarMetadatoPublisher publisher; 
    private final TelemetryService telemetry;
    public ActualizarMetadatoImpl(final MetadatoRepository r, final ActualizarMetadatoRuleValidator v,
            final ActualizarMetadatoPublisher p, final TelemetryService t) { repository = r; validator = v; publisher = p; telemetry = t; }
    @Override public void execute(final ActualizarMetadatoDomain data) {
        telemetry.recordBusinessOperation("actualizar-metadato", () -> {
            validator.validate(data);
            var updated = repository.update(MetadatoEntity.create(data.getId(), data.getIdParametro(), data.getIdTipoMetadato(), data.getValor()));
            publisher.sendEvent(ActualizarMetadatoEvent.updated(updated));
        });
    }
}
