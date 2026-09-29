package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.usecase.domain.ActualizarMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.usecase.domain.rules.ActualizarMetadatoAmbienteExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class ActualizarMetadatoAmbienteExistsRuleImpl implements ActualizarMetadatoAmbienteExistsRule {
    private final MetadatoAmbienteRepository repository;

    public ActualizarMetadatoAmbienteExistsRuleImpl(final MetadatoAmbienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(final ActualizarMetadatoAmbienteDomain data) {
        if (repository.findById(data.getId()).isEmpty()) {
            throw NotFoundException.build("No se encontro el metadato de ambiente.");
        }
    }
}

