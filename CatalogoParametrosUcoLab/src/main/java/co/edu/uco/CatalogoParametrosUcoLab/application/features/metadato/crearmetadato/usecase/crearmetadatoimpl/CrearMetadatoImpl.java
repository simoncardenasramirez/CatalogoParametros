package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.crearmetadatoimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.CrearMetadato;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.CrearMetadatoRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.secondaryports.event.CrearMetadatoEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.secondaryports.publisher.CrearMetadatoPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.domain.CrearMetadatoDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoRepository;

@Service
public final class CrearMetadatoImpl implements CrearMetadato {
    private final MetadatoRepository repository;
    private final CrearMetadatoRuleValidator validator;
    private final CrearMetadatoPublisher publisher;
    private final TelemetryService telemetry;
    public CrearMetadatoImpl(final MetadatoRepository repository, final CrearMetadatoRuleValidator validator,
            final CrearMetadatoPublisher publisher, final TelemetryService telemetry) {
        this.repository = repository; this.validator = validator; this.publisher = publisher; this.telemetry = telemetry;
    }
    @Override public void execute(final CrearMetadatoDomain data) {
        telemetry.recordBusinessOperation("crear-metadato", () -> {
            validator.validate(data);
            data.generateId();
            var saved = repository.save(MetadatoEntity.create(data.getId(), data.getIdParametro(),
                    data.getIdTipoMetadato(), data.getValor()));
            publisher.sendEvent(CrearMetadatoEvent.created(saved));
        });
    }
}
