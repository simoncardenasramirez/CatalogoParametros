package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.usecase.actualizarestadoambienteimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.ActualizarEstadoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.secondaryports.event.ActualizarEstadoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.secondaryports.publisher.ActualizarEstadoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.usecase.domain.ActualizarEstadoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class ActualizarEstadoAmbienteImpl implements ActualizarEstadoAmbiente {
    private final EstadoAmbienteRepository repository;
    private final ActualizarEstadoAmbientePublisher publisher;

    public ActualizarEstadoAmbienteImpl(final EstadoAmbienteRepository repository, final ActualizarEstadoAmbientePublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Override
    public EstadoAmbienteEntity execute(final ActualizarEstadoAmbienteDomain data) {
        final var current = repository.findById(data.getId())
                .orElseThrow(() -> NotFoundException.build("No se encontro estadoambiente."));
        if (!current.getNombre().equalsIgnoreCase(data.getNombre())
                && repository.existsByNombre(data.getNombre())) {
            throw ConflictException.build("Ya existe estadoambiente con el nombre indicado.");
        }
        final var entity = repository.update(EstadoAmbienteEntity.create(data.getId(), data.getNombre()));
        publisher.sendEvent(ActualizarEstadoAmbienteEvent.updated(entity));
        return entity;
    }
}
