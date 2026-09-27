package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.domain.CrearMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.domain.rules.MetadatoAmbienteEstadoExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class MetadatoAmbienteEstadoExistsRuleImpl implements MetadatoAmbienteEstadoExistsRule {
    private final EstadoMetadatoAmbienteRepository repository;

    public MetadatoAmbienteEstadoExistsRuleImpl(final EstadoMetadatoAmbienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(final CrearMetadatoAmbienteDomain data) {
        if (repository.findById(data.getIdEstadoMetadatoAmbiente()).isEmpty()) {
            throw NotFoundException.build("No se encontro el estado de metadato ambiente indicado.");
        }
    }
}

