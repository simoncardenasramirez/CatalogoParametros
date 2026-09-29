package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.usecase.domain.CrearEstadoMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.usecase.domain.rules.EstadoMetadatoAmbienteNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@Service
public final class EstadoMetadatoAmbienteNameDoesNotExistRuleImpl implements EstadoMetadatoAmbienteNameDoesNotExistRule {
    private final EstadoMetadatoAmbienteRepository repository;

    public EstadoMetadatoAmbienteNameDoesNotExistRuleImpl(final EstadoMetadatoAmbienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(final CrearEstadoMetadatoAmbienteDomain data) {
        if (repository.existsByNombre(data.getNombre())) {
            throw ConflictException.build("Ya existe estadometadatoambiente con el nombre indicado.");
        }
    }
}

