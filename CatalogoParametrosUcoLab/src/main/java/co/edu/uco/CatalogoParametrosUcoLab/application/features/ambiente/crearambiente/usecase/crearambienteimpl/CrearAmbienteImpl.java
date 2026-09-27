package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.usecase.crearambienteimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.CrearAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.secondaryports.event.CrearAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.secondaryports.publisher.CrearAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.usecase.domain.CrearAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class CrearAmbienteImpl implements CrearAmbiente {
    private final AmbienteRepository repository;
    private final CrearAmbientePublisher publisher;

    public CrearAmbienteImpl(final AmbienteRepository repository, final CrearAmbientePublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Override
    public AmbienteEntity execute(final CrearAmbienteDomain data) {
        if (repository.existsByNombre(data.getNombre())) {
            throw ConflictException.build("Ya existe ambiente con el nombre indicado.");
        }
        final var entity = repository.save(AmbienteEntity.create(UUIDHelper.generate(), data.getNombre()));
        publisher.sendEvent(CrearAmbienteEvent.created(entity));
        return entity;
    }
}
