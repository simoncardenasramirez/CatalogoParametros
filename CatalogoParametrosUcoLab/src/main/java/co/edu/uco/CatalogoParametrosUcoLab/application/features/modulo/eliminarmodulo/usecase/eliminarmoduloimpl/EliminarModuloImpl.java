package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.eliminarmodulo.usecase.eliminarmoduloimpl;

import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.eliminarmodulo.EliminarModulo;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.eliminarmodulo.secondaryports.event.EliminarModuloEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.eliminarmodulo.secondaryports.publisher.EliminarModuloPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;
@Service
public final class EliminarModuloImpl implements EliminarModulo {
    private static final Logger LOGGER = LoggerFactory.getLogger(EliminarModuloImpl.class);
    private final ModuloRepository repository;
    private final EliminarModuloPublisher publisher;
    private final TelemetryService telemetry;
    public EliminarModuloImpl(final ModuloRepository repository, final EliminarModuloPublisher publisher,
            final TelemetryService telemetry) { this.repository = repository; this.publisher = publisher; this.telemetry = telemetry; }
    @Override public void execute(final UUID id) {
        telemetry.recordBusinessOperation("eliminar-modulo", () -> {
            if (id == null || UUIDHelper.getDefault().equals(id)) throw ValidationException.build("El identificador del modulo es obligatorio.");
            var modulo = repository.findById(id).orElseThrow(() -> NotFoundException.build("El modulo no existe."));
            repository.deleteById(id);
            publisher.sendEvent(EliminarModuloEvent.deleted(modulo));
            LOGGER.info("[ELIMINAR-MODULO] Modulo eliminado exitosamente con id: {}", id);
        });
    }
}
