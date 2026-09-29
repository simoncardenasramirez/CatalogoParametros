package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.usecase.domain.CrearEstadoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.usecase.domain.rules.EstadoAmbienteNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@Service
public final class EstadoAmbienteNameDoesNotExistRuleImpl implements EstadoAmbienteNameDoesNotExistRule {
    private final EstadoAmbienteRepository repository;

    public EstadoAmbienteNameDoesNotExistRuleImpl(final EstadoAmbienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(final CrearEstadoAmbienteDomain data) {
        if (repository.existsByNombre(data.getNombre())) {
            throw ConflictException.build("Ya existe estadoambiente con el nombre indicado.");
        }
    }
}

