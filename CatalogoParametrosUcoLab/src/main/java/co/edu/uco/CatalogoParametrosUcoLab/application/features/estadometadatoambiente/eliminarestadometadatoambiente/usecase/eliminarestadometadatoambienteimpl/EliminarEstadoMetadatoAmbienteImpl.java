package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.eliminarestadometadatoambiente.usecase.eliminarestadometadatoambienteimpl;

import java.util.UUID;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.eliminarestadometadatoambiente.EliminarEstadoMetadatoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.eliminarestadometadatoambiente.secondaryports.event.EliminarEstadoMetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.eliminarestadometadatoambiente.secondaryports.publisher.EliminarEstadoMetadatoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class EliminarEstadoMetadatoAmbienteImpl implements EliminarEstadoMetadatoAmbiente {
    private final EstadoMetadatoAmbienteRepository repository;
    private final EliminarEstadoMetadatoAmbientePublisher publisher;
    private final MetadatoAmbienteRepository metadatoRepository;

    public EliminarEstadoMetadatoAmbienteImpl(final EstadoMetadatoAmbienteRepository repository, final EliminarEstadoMetadatoAmbientePublisher publisher, final MetadatoAmbienteRepository metadatoRepository) {
        this.repository = repository;
        this.publisher = publisher;
        this.metadatoRepository = metadatoRepository;
    }

    @Override public void execute(final UUID id) {
        final var entity = repository.findById(id)
                .orElseThrow(() -> NotFoundException.build("No se encontro estadometadatoambiente."));
        if (metadatoRepository.existsByIdEstadoMetadatoAmbiente(id)) {
            throw ConflictException.build("No se puede eliminar el estado porque tiene metadatos asociados.");
        }
        repository.deleteById(id);
        publisher.sendEvent(EliminarEstadoMetadatoAmbienteEvent.deleted(entity));
    }
}
