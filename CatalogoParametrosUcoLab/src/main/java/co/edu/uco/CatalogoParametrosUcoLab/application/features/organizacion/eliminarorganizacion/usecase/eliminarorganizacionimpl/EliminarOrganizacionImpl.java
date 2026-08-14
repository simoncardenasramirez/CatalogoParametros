package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.usecase.eliminarorganizacionimpl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.EliminarOrganizacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.secondaryports.event.EliminarOrganizacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.secondaryports.publisher.EliminarOrganizacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.usecase.domain.rules.EliminarOrganizacionIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.usecase.domain.rules.EliminarOrganizacionIsNotUsedByAplicacionRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;

@Service
public class EliminarOrganizacionImpl implements EliminarOrganizacion {

    private static final Logger logger = LoggerFactory.getLogger(EliminarOrganizacionImpl.class);
    private static final String OPERATION_NAME = "eliminar-organizacion";

    private final OrganizacionRepository organizacionRepository;
    private final EliminarOrganizacionPublisher eliminarOrganizacionPublisher;
    private final EliminarOrganizacionIdExistsRule idExistsRule;
    private final EliminarOrganizacionIsNotUsedByAplicacionRule isNotUsedByAplicacionRule;
    private final TelemetryService telemetryService;

    public EliminarOrganizacionImpl(final OrganizacionRepository organizacionRepository,
            final EliminarOrganizacionPublisher eliminarOrganizacionPublisher,
            final EliminarOrganizacionIdExistsRule idExistsRule,
            final EliminarOrganizacionIsNotUsedByAplicacionRule isNotUsedByAplicacionRule,
            final TelemetryService telemetryService) {
        this.organizacionRepository = organizacionRepository;
        this.eliminarOrganizacionPublisher = eliminarOrganizacionPublisher;
        this.idExistsRule = idExistsRule;
        this.isNotUsedByAplicacionRule = isNotUsedByAplicacionRule;
        this.telemetryService = telemetryService;
    }

    @Override
    @Transactional
    public void execute(final java.util.UUID id) {
        telemetryService.recordBusinessOperation(OPERATION_NAME, () -> {
            logger.info("[ELIMINAR-ORGANIZACION] Iniciando eliminacion de organizacion con id: {}", id);
            final List<String> messages = new ArrayList<>();
            try {
                idExistsRule.execute(id);
            } catch (final Exception e) {
                messages.add(e.getMessage());
            }
            try {
                isNotUsedByAplicacionRule.execute(id);
            } catch (final Exception e) {
                messages.add(e.getMessage());
            }
            if (!messages.isEmpty()) {
                throw ValidationException.build(String.join(", ", messages));
            }

            var organizacion = organizacionRepository.findById(id)
                    .orElseThrow(() -> NotFoundException.build("No existe una organizacion con el id especificado."));

            organizacionRepository.deleteById(id);
            eliminarOrganizacionPublisher.sendEvent(EliminarOrganizacionEvent.deleted(organizacion));
            logger.info("[ELIMINAR-ORGANIZACION] Organizacion eliminada exitosamente con id: {}", id);
        });
    }
}
