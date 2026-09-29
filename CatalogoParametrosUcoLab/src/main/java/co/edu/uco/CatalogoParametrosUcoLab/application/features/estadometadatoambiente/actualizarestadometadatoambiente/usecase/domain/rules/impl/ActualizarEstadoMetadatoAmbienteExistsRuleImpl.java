package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.usecase.domain.ActualizarEstadoMetadatoAmbienteDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.usecase.domain.rules.ActualizarEstadoMetadatoAmbienteExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class ActualizarEstadoMetadatoAmbienteExistsRuleImpl implements ActualizarEstadoMetadatoAmbienteExistsRule {
    private final EstadoMetadatoAmbienteRepository repository;

    public ActualizarEstadoMetadatoAmbienteExistsRuleImpl(final EstadoMetadatoAmbienteRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(final ActualizarEstadoMetadatoAmbienteDomain data) {
        if (repository.findById(data.getId()).isEmpty()) {
            throw NotFoundException.build("No se encontro estadometadatoambiente.");
        }
    }
}

