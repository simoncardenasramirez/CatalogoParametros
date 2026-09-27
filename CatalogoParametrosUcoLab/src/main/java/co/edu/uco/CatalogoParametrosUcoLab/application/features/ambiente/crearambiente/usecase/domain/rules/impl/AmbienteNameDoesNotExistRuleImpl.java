package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.usecase.domain.CrearAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.usecase.domain.rules.AmbienteNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@Service
public final class AmbienteNameDoesNotExistRuleImpl implements AmbienteNameDoesNotExistRule {
    private final AmbienteRepository repository;

    public AmbienteNameDoesNotExistRuleImpl(final AmbienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(final CrearAmbienteDomain data) {
        if (repository.existsByNombre(data.getNombre())) {
            throw ConflictException.build("Ya existe ambiente con el nombre indicado.");
        }
    }
}

