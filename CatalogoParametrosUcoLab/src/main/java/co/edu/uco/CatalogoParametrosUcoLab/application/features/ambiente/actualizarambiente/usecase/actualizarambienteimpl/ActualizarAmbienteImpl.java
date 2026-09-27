package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.usecase.actualizarambienteimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.ActualizarAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.secondaryports.event.ActualizarAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.secondaryports.publisher.ActualizarAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.usecase.domain.ActualizarAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class ActualizarAmbienteImpl implements ActualizarAmbiente {
    private final AmbienteRepository repository;
    private final ActualizarAmbientePublisher publisher;

    public ActualizarAmbienteImpl(final AmbienteRepository repository, final ActualizarAmbientePublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Override
    public AmbienteEntity execute(final ActualizarAmbienteDomain data) {
        final var current = repository.findById(data.getId())
                .orElseThrow(() -> NotFoundException.build("No se encontro ambiente."));
        if (!current.getNombre().equalsIgnoreCase(data.getNombre())
                && repository.existsByNombre(data.getNombre())) {
            throw ConflictException.build("Ya existe ambiente con el nombre indicado.");
        }
        final var entity = repository.update(AmbienteEntity.create(data.getId(), data.getNombre()));
        publisher.sendEvent(ActualizarAmbienteEvent.updated(entity));
        return entity;
    }
}
