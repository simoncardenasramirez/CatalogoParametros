package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.usecase.domain.ActualizarEstadoMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.usecase.domain.rules.ActualizarEstadoMetadatoAmbienteNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ConflictException;

@Service
public final class ActualizarEstadoMetadatoAmbienteNameDoesNotExistRuleImpl implements ActualizarEstadoMetadatoAmbienteNameDoesNotExistRule {
    private final EstadoMetadatoAmbienteRepository repository;

    public ActualizarEstadoMetadatoAmbienteNameDoesNotExistRuleImpl(final EstadoMetadatoAmbienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(final ActualizarEstadoMetadatoAmbienteDomain data) {
        final var current = repository.findById(data.getId()).orElse(null);
        if (current != null && !current.getNombre().equalsIgnoreCase(data.getNombre())
                && repository.existsByNombre(data.getNombre())) {
            throw ConflictException.build("Ya existe estadometadatoambiente con el nombre indicado.");
        }
    }
}

