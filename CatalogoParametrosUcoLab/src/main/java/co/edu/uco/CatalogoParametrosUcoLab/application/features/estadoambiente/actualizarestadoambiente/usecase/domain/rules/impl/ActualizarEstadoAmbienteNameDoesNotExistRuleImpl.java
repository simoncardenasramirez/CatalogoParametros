package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.usecase.domain.ActualizarEstadoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.usecase.domain.rules.ActualizarEstadoAmbienteNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@Service
public final class ActualizarEstadoAmbienteNameDoesNotExistRuleImpl implements ActualizarEstadoAmbienteNameDoesNotExistRule {
    private final EstadoAmbienteRepository repository;

    public ActualizarEstadoAmbienteNameDoesNotExistRuleImpl(final EstadoAmbienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(final ActualizarEstadoAmbienteDomain data) {
        final var current = repository.findById(data.getId()).orElse(null);
        if (current != null && !current.getNombre().equalsIgnoreCase(data.getNombre())
                && repository.existsByNombre(data.getNombre())) {
            throw ConflictException.build("Ya existe estadoambiente con el nombre indicado.");
        }
    }
}

