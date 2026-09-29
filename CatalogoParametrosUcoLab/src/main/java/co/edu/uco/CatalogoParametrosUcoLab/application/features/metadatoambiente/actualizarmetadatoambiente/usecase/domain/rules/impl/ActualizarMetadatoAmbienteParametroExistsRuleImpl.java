package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.usecase.domain.ActualizarMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.usecase.domain.rules.ActualizarMetadatoAmbienteParametroExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class ActualizarMetadatoAmbienteParametroExistsRuleImpl implements ActualizarMetadatoAmbienteParametroExistsRule {
    private final ParametroRepository repository;

    public ActualizarMetadatoAmbienteParametroExistsRuleImpl(final ParametroRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(final ActualizarMetadatoAmbienteDomain data) {
        if (repository.findById(data.getIdParametro()).isEmpty()) {
            throw NotFoundException.build("No se encontro el parametro indicado.");
        }
    }
}

