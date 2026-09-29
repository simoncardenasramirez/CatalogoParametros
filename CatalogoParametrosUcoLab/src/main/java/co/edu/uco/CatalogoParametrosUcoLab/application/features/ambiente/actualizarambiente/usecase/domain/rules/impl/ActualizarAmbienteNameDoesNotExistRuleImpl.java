package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.usecase.domain.ActualizarAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.usecase.domain.rules.ActualizarAmbienteNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@Service
public final class ActualizarAmbienteNameDoesNotExistRuleImpl implements ActualizarAmbienteNameDoesNotExistRule {
    private final AmbienteRepository repository;

    public ActualizarAmbienteNameDoesNotExistRuleImpl(final AmbienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(final ActualizarAmbienteDomain data) {
        final var current = repository.findById(data.getId()).orElse(null);
        if (current != null && !current.getNombre().equalsIgnoreCase(data.getNombre())
                && repository.existsByNombre(data.getNombre())) {
            throw ConflictException.build("Ya existe ambiente con el nombre indicado.");
        }
    }
}

