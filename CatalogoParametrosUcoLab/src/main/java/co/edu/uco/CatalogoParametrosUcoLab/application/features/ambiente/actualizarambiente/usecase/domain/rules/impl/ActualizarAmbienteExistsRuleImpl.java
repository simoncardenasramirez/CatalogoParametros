package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.usecase.domain.ActualizarAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.usecase.domain.rules.ActualizarAmbienteExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class ActualizarAmbienteExistsRuleImpl implements ActualizarAmbienteExistsRule {
    private final AmbienteRepository repository;

    public ActualizarAmbienteExistsRuleImpl(final AmbienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(final ActualizarAmbienteDomain data) {
        if (repository.findById(data.getId()).isEmpty()) {
            throw NotFoundException.build("No se encontro ambiente.");
        }
    }
}

