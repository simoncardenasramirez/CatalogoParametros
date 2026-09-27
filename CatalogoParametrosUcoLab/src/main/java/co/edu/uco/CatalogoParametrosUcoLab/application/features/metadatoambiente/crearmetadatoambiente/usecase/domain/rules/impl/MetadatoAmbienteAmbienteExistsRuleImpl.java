package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.domain.CrearMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.domain.rules.MetadatoAmbienteAmbienteExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class MetadatoAmbienteAmbienteExistsRuleImpl implements MetadatoAmbienteAmbienteExistsRule {
    private final AmbienteRepository repository;

    public MetadatoAmbienteAmbienteExistsRuleImpl(final AmbienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(final CrearMetadatoAmbienteDomain data) {
        if (repository.findById(data.getIdAmbiente()).isEmpty()) {
            throw NotFoundException.build("No se encontro el ambiente indicado.");
        }
    }
}

