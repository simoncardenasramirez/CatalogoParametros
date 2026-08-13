package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.usecase.eliminaraimpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.EliminarAplicacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.secondaryports.event.EliminarAplicacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.secondaryports.publisher.EliminarAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.usecase.domain.rules.EliminarAplicacionIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.usecase.domain.rules.EliminarAplicacionIsNotUsedByModuloRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;

@Service
public class EliminarAplicacionImpl implements EliminarAplicacion {

    private final AplicacionRepository aplicacionRepository;
    private final EliminarAplicacionPublisher eliminarAplicacionPublisher;
    private final EliminarAplicacionIdExistsRule idExistsRule;
    private final EliminarAplicacionIsNotUsedByModuloRule isNotUsedByModuloRule;

    public EliminarAplicacionImpl(final AplicacionRepository aplicacionRepository,
            final EliminarAplicacionPublisher eliminarAplicacionPublisher,
            final EliminarAplicacionIdExistsRule idExistsRule,
            final EliminarAplicacionIsNotUsedByModuloRule isNotUsedByModuloRule) {
        this.aplicacionRepository = aplicacionRepository;
        this.eliminarAplicacionPublisher = eliminarAplicacionPublisher;
        this.idExistsRule = idExistsRule;
        this.isNotUsedByModuloRule = isNotUsedByModuloRule;
    }

    @Override
    @Transactional
    public void execute(final java.util.UUID id) {
        final List<String> messages = new ArrayList<>();
        try {
            idExistsRule.execute(id);
        } catch (final Exception e) {
            messages.add(e.getMessage());
        }
        try {
            isNotUsedByModuloRule.execute(id);
        } catch (final Exception e) {
            messages.add(e.getMessage());
        }
        if (!messages.isEmpty()) {
            throw ValidationException.build(String.join(", ", messages));
        }

        final AplicacionEntity aplicacion = aplicacionRepository.findById(id).orElseThrow();
        aplicacionRepository.deleteById(id);
        eliminarAplicacionPublisher.sendEvent(EliminarAplicacionEvent.deleted(aplicacion));
    }
}
