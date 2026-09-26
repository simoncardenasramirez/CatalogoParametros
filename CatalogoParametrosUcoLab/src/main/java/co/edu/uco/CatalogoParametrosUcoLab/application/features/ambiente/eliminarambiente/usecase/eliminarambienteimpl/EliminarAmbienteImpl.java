package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.eliminarambiente.usecase.eliminarambienteimpl;

import java.util.UUID;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.eliminarambiente.EliminarAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.eliminarambiente.secondaryports.event.EliminarAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.eliminarambiente.secondaryports.publisher.EliminarAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class EliminarAmbienteImpl implements EliminarAmbiente {
    private final AmbienteRepository repository;
    private final EliminarAmbientePublisher publisher;
    private final MetadatoAmbienteRepository metadatoRepository;

    public EliminarAmbienteImpl(final AmbienteRepository repository, final EliminarAmbientePublisher publisher, final MetadatoAmbienteRepository metadatoRepository) {
        this.repository = repository;
        this.publisher = publisher;
        this.metadatoRepository = metadatoRepository;
    }

    @Override public void execute(final UUID id) {
        final var entity = repository.findById(id)
                .orElseThrow(() -> NotFoundException.build("No se encontro ambiente."));
        if (metadatoRepository.existsByIdAmbiente(id)) {
            throw ConflictException.build("No se puede eliminar el ambiente porque tiene metadatos asociados.");
        }
        repository.deleteById(id);
        publisher.sendEvent(EliminarAmbienteEvent.deleted(entity));
    }
}
