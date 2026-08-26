package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.eliminarmetadato.usecase.eliminarmetadatoimpl;

import java.util.UUID;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.eliminarmetadato.EliminarMetadato;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.eliminarmetadato.secondaryports.event.EliminarMetadatoEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.eliminarmetadato.secondaryports.publisher.EliminarMetadatoPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class EliminarMetadatoImpl implements EliminarMetadato {
    private final MetadatoRepository repository; private final EliminarMetadatoPublisher publisher; private final TelemetryService telemetry;
    public EliminarMetadatoImpl(final MetadatoRepository repository, final EliminarMetadatoPublisher publisher, final TelemetryService telemetry) {
        this.repository = repository; this.publisher = publisher; this.telemetry = telemetry;
    }
    @Override public void execute(final UUID id) {
        telemetry.recordBusinessOperation("eliminar-metadato", () -> {
            var entity = repository.findById(id).orElseThrow(() -> NotFoundException.build("El metadato no existe."));
            repository.deleteById(id); publisher.sendEvent(EliminarMetadatoEvent.deleted(entity));
        });
    }
}
