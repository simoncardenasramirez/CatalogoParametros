package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.usecase.domain.ActualizarMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.usecase.domain.rules.ActualizarMetadatoAmbienteEstadoExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class ActualizarMetadatoAmbienteEstadoExistsRuleImpl implements ActualizarMetadatoAmbienteEstadoExistsRule {
    private final EstadoMetadatoAmbienteRepository repository;

    public ActualizarMetadatoAmbienteEstadoExistsRuleImpl(final EstadoMetadatoAmbienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(final ActualizarMetadatoAmbienteDomain data) {
        if (repository.findById(data.getIdEstadoMetadatoAmbiente()).isEmpty()) {
            throw NotFoundException.build("No se encontro el estado de metadato ambiente indicado.");
        }
    }
}

