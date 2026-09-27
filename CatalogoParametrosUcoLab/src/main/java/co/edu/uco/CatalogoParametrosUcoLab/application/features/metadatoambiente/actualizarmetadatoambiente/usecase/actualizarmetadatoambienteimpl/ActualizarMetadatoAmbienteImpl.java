package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.usecase.actualizarmetadatoambienteimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.ActualizarMetadatoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.secondaryports.event.ActualizarMetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.secondaryports.publisher.ActualizarMetadatoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.usecase.domain.ActualizarMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class ActualizarMetadatoAmbienteImpl implements ActualizarMetadatoAmbiente {
    private final MetadatoAmbienteRepository repository;
    private final ParametroRepository parametroRepository;
    private final AmbienteRepository ambienteRepository;
    private final EstadoMetadatoAmbienteRepository estadoRepository;
    private final ActualizarMetadatoAmbientePublisher publisher;

    public ActualizarMetadatoAmbienteImpl(final MetadatoAmbienteRepository repository,
            final ParametroRepository parametroRepository, final AmbienteRepository ambienteRepository,
            final EstadoMetadatoAmbienteRepository estadoRepository, final ActualizarMetadatoAmbientePublisher publisher) {
        this.repository = repository;
        this.parametroRepository = parametroRepository;
        this.ambienteRepository = ambienteRepository;
        this.estadoRepository = estadoRepository;
        this.publisher = publisher;
    }

    @Override
    public MetadatoAmbienteEntity execute(final ActualizarMetadatoAmbienteDomain data) {
        repository.findById(data.getId())
                .orElseThrow(() -> NotFoundException.build("No se encontro el metadato de ambiente."));
        validateRelations(data);
        final var entity = repository.update(MetadatoAmbienteEntity.create(data.getId(),
                data.getIdParametro(), data.getIdAmbiente(), data.getIdEstadoMetadatoAmbiente()));
        publisher.sendEvent(ActualizarMetadatoAmbienteEvent.updated(entity));
        return entity;
    }

    private void validateRelations(final ActualizarMetadatoAmbienteDomain data) {
        if (parametroRepository.findById(data.getIdParametro()).isEmpty()) {
            throw NotFoundException.build("No se encontro el parametro indicado.");
        }
        if (ambienteRepository.findById(data.getIdAmbiente()).isEmpty()) {
            throw NotFoundException.build("No se encontro el ambiente indicado.");
        }
        if (estadoRepository.findById(data.getIdEstadoMetadatoAmbiente()).isEmpty()) {
            throw NotFoundException.build("No se encontro el estado de metadato ambiente indicado.");
        }
    }
}
