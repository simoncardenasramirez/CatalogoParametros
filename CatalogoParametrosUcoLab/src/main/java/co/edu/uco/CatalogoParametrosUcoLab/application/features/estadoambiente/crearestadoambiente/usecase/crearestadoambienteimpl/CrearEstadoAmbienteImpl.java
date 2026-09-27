package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.usecase.crearestadoambienteimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.CrearEstadoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.secondaryports.event.CrearEstadoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.secondaryports.publisher.CrearEstadoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.usecase.domain.CrearEstadoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class CrearEstadoAmbienteImpl implements CrearEstadoAmbiente {
    private final EstadoAmbienteRepository repository;
    private final CrearEstadoAmbientePublisher publisher;

    public CrearEstadoAmbienteImpl(final EstadoAmbienteRepository repository, final CrearEstadoAmbientePublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Override
    public EstadoAmbienteEntity execute(final CrearEstadoAmbienteDomain data) {
        if (repository.existsByNombre(data.getNombre())) {
            throw ConflictException.build("Ya existe estadoambiente con el nombre indicado.");
        }
        final var entity = repository.save(EstadoAmbienteEntity.create(UUIDHelper.generate(), data.getNombre()));
        publisher.sendEvent(CrearEstadoAmbienteEvent.created(entity));
        return entity;
    }
}
