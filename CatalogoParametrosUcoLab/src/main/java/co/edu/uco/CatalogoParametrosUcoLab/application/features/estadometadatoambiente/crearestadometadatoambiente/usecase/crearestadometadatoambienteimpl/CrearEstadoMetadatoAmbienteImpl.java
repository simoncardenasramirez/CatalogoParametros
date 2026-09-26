package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.usecase.crearestadometadatoambienteimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.CrearEstadoMetadatoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.secondaryports.event.CrearEstadoMetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.secondaryports.publisher.CrearEstadoMetadatoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.usecase.domain.CrearEstadoMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class CrearEstadoMetadatoAmbienteImpl implements CrearEstadoMetadatoAmbiente {
    private final EstadoMetadatoAmbienteRepository repository;
    private final CrearEstadoMetadatoAmbientePublisher publisher;

    public CrearEstadoMetadatoAmbienteImpl(final EstadoMetadatoAmbienteRepository repository, final CrearEstadoMetadatoAmbientePublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Override
    public EstadoMetadatoAmbienteEntity execute(final CrearEstadoMetadatoAmbienteDomain data) {
        if (repository.existsByNombre(data.getNombre())) {
            throw ConflictException.build("Ya existe estadometadatoambiente con el nombre indicado.");
        }
        final var entity = repository.save(EstadoMetadatoAmbienteEntity.create(UUIDHelper.generate(), data.getNombre()));
        publisher.sendEvent(CrearEstadoMetadatoAmbienteEvent.created(entity));
        return entity;
    }
}
