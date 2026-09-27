package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.crearmetadatoambienteimpl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.CrearMetadatoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.secondaryports.event.CrearMetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.secondaryports.publisher.CrearMetadatoAmbientePublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.domain.CrearMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class CrearMetadatoAmbienteImpl implements CrearMetadatoAmbiente {
    private final MetadatoAmbienteRepository repository;
    private final ParametroRepository parametroRepository;
    private final AmbienteRepository ambienteRepository;
    private final EstadoMetadatoAmbienteRepository estadoRepository;
    private final CrearMetadatoAmbientePublisher publisher;

    public CrearMetadatoAmbienteImpl(final MetadatoAmbienteRepository repository,
            final ParametroRepository parametroRepository, final AmbienteRepository ambienteRepository,
            final EstadoMetadatoAmbienteRepository estadoRepository, final CrearMetadatoAmbientePublisher publisher) {
        this.repository = repository;
        this.parametroRepository = parametroRepository;
        this.ambienteRepository = ambienteRepository;
        this.estadoRepository = estadoRepository;
        this.publisher = publisher;
    }

    @Override
    public MetadatoAmbienteEntity execute(final CrearMetadatoAmbienteDomain data) {
        validateRelations(data);
        final var entity = repository.save(MetadatoAmbienteEntity.create(UUIDHelper.generate(),
                data.getIdParametro(), data.getIdAmbiente(), data.getIdEstadoMetadatoAmbiente()));
        publisher.sendEvent(CrearMetadatoAmbienteEvent.created(entity));
        return entity;
    }

    private void validateRelations(final CrearMetadatoAmbienteDomain data) {
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
