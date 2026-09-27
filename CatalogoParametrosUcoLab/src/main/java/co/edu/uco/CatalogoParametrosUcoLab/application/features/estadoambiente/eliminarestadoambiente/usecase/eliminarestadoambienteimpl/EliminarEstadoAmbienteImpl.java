package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.eliminarestadoambiente.usecase.eliminarestadoambienteimpl;

import java.util.UUID;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.eliminarestadoambiente.EliminarEstadoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.eliminarestadoambiente.secondaryports.event.EliminarEstadoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.eliminarestadoambiente.secondaryports.publisher.EliminarEstadoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class EliminarEstadoAmbienteImpl implements EliminarEstadoAmbiente {
    private final EstadoAmbienteRepository repository;
    private final EliminarEstadoAmbientePublisher publisher;
    

    public EliminarEstadoAmbienteImpl(final EstadoAmbienteRepository repository, final EliminarEstadoAmbientePublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
        
    }

    @Override public void execute(final UUID id) {
        final var entity = repository.findById(id)
                .orElseThrow(() -> NotFoundException.build("No se encontro estadoambiente."));
        
        repository.deleteById(id);
        publisher.sendEvent(EliminarEstadoAmbienteEvent.deleted(entity));
    }
}
