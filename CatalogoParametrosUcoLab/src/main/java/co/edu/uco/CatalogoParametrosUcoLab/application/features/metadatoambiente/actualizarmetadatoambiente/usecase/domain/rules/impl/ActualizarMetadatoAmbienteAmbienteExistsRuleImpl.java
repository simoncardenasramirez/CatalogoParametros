package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.usecase.domain.ActualizarMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.usecase.domain.rules.ActualizarMetadatoAmbienteAmbienteExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class ActualizarMetadatoAmbienteAmbienteExistsRuleImpl implements ActualizarMetadatoAmbienteAmbienteExistsRule {
    private final AmbienteRepository repository;

    public ActualizarMetadatoAmbienteAmbienteExistsRuleImpl(final AmbienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(final ActualizarMetadatoAmbienteDomain data) {
        if (repository.findById(data.getIdAmbiente()).isEmpty()) {
            throw NotFoundException.build("No se encontro el ambiente indicado.");
        }
    }
}

