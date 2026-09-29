package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.usecase.domain.ActualizarEstadoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.usecase.domain.rules.ActualizarEstadoAmbienteExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class ActualizarEstadoAmbienteExistsRuleImpl implements ActualizarEstadoAmbienteExistsRule {
    private final EstadoAmbienteRepository repository;

    public ActualizarEstadoAmbienteExistsRuleImpl(final EstadoAmbienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(final ActualizarEstadoAmbienteDomain data) {
        if (repository.findById(data.getId()).isEmpty()) {
            throw NotFoundException.build("No se encontro estadoambiente.");
        }
    }
}

