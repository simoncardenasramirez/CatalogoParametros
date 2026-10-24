package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.domain.rules.impl;

import java.util.UUID;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.domain.rules.MetadatoAmbienteExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

@Service
public final class MetadatoAmbienteExistsRuleImpl implements MetadatoAmbienteExistsRule {
    private final AmbienteRepository ambientes;
    public MetadatoAmbienteExistsRuleImpl(final AmbienteRepository ambientes) { this.ambientes = ambientes; }
    @Override public void execute(final UUID idAmbiente) {
        if (idAmbiente == null) throw ValidationException.build("El ambiente del metadato es obligatorio.");
        if (ambientes.findById(idAmbiente).isEmpty()) throw NotFoundException.build("El ambiente del metadato no existe.");
    }
}
