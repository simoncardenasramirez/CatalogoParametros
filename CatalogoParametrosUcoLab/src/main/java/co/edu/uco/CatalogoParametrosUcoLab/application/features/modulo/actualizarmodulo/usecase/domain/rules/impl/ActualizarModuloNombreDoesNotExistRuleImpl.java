package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.ActualizarModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.ActualizarModuloNombreDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class ActualizarModuloNombreDoesNotExistRuleImpl implements ActualizarModuloNombreDoesNotExistRule {

    private final ModuloRepository moduloRepository;

    public ActualizarModuloNombreDoesNotExistRuleImpl(final ModuloRepository moduloRepository) {
        this.moduloRepository = moduloRepository;
    }

    @Override
    public void execute(final ActualizarModuloDomain data) {
        final var nombre = TextHelper.applyTrim(data.getNombre());
        final var id = data.getId();
        if (moduloRepository.existsByNombre(nombre) && !UUIDHelper.getDefault().equals(id)) {
            throw new co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.exception.ModuloException(
                    "El nombre del modulo ya existe en el sistema.");
        }
    }
}
