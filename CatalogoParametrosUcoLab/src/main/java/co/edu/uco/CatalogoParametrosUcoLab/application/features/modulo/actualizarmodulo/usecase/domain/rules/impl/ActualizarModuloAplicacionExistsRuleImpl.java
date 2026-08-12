package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.ActualizarModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.ActualizarModuloAplicacionExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class ActualizarModuloAplicacionExistsRuleImpl implements ActualizarModuloAplicacionExistsRule {

    private final AplicacionRepository aplicacionRepository;

    public ActualizarModuloAplicacionExistsRuleImpl(final AplicacionRepository aplicacionRepository) {
        this.aplicacionRepository = aplicacionRepository;
    }

    @Override
    public void execute(final ActualizarModuloDomain data) {
        final var idAplicacion = data.getIdAplicacion();
        if (UUIDHelper.getDefault().equals(idAplicacion)) {
            throw NotFoundException.build("La aplicacion asociada al modulo es obligatoria.");
        }
        if (aplicacionRepository.findById(idAplicacion).isEmpty()) {
            throw NotFoundException.build("La aplicacion asociada al modulo no existe.");
        }
    }
}
