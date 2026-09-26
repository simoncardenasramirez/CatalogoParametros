package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.eliminarmetadatoambiente.usecase.eliminarmetadatoambienteimpl;

import java.util.UUID;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.eliminarmetadatoambiente.EliminarMetadatoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.eliminarmetadatoambiente.secondaryports.event.EliminarMetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.eliminarmetadatoambiente.secondaryports.publisher.EliminarMetadatoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class EliminarMetadatoAmbienteImpl implements EliminarMetadatoAmbiente {
    private final MetadatoAmbienteRepository repository;
    private final EliminarMetadatoAmbientePublisher publisher;
    

    public EliminarMetadatoAmbienteImpl(final MetadatoAmbienteRepository repository, final EliminarMetadatoAmbientePublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
        
    }

    @Override public void execute(final UUID id) {
        final var entity = repository.findById(id)
                .orElseThrow(() -> NotFoundException.build("No se encontro metadatoambiente."));
        
        repository.deleteById(id);
        publisher.sendEvent(EliminarMetadatoAmbienteEvent.deleted(entity));
    }
}
