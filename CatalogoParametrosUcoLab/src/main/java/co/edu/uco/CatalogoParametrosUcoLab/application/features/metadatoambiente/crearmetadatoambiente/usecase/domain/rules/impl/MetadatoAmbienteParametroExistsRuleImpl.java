package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.domain.CrearMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.usecase.domain.rules.MetadatoAmbienteParametroExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class MetadatoAmbienteParametroExistsRuleImpl implements MetadatoAmbienteParametroExistsRule {
    private final ParametroRepository repository;

    public MetadatoAmbienteParametroExistsRuleImpl(final ParametroRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(final CrearMetadatoAmbienteDomain data) {
        if (repository.findById(data.getIdParametro()).isEmpty()) {
            throw NotFoundException.build("No se encontro el parametro indicado.");
        }
    }
}

