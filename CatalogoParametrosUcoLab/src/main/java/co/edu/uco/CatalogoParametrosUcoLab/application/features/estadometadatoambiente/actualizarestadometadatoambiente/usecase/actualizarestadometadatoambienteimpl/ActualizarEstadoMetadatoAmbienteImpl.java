package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.usecase.actualizarestadometadatoambienteimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.ActualizarEstadoMetadatoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.secondaryports.event.ActualizarEstadoMetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.secondaryports.publisher.ActualizarEstadoMetadatoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.usecase.domain.ActualizarEstadoMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class ActualizarEstadoMetadatoAmbienteImpl implements ActualizarEstadoMetadatoAmbiente {
    private final EstadoMetadatoAmbienteRepository repository;
    private final ActualizarEstadoMetadatoAmbientePublisher publisher;

    public ActualizarEstadoMetadatoAmbienteImpl(final EstadoMetadatoAmbienteRepository repository, final ActualizarEstadoMetadatoAmbientePublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Override
    public EstadoMetadatoAmbienteEntity execute(final ActualizarEstadoMetadatoAmbienteDomain data) {
        final var current = repository.findById(data.getId())
                .orElseThrow(() -> NotFoundException.build("No se encontro estadometadatoambiente."));
        if (!current.getNombre().equalsIgnoreCase(data.getNombre())
                && repository.existsByNombre(data.getNombre())) {
            throw ConflictException.build("Ya existe estadometadatoambiente con el nombre indicado.");
        }
        final var entity = repository.update(EstadoMetadatoAmbienteEntity.create(data.getId(), data.getNombre()));
        publisher.sendEvent(ActualizarEstadoMetadatoAmbienteEvent.updated(entity));
        return entity;
    }
}
