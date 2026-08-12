package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.ActualizarModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.ActualizarModuloIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class ActualizarModuloIdExistsRuleImpl implements ActualizarModuloIdExistsRule {

    private final ModuloRepository moduloRepository;

    public ActualizarModuloIdExistsRuleImpl(final ModuloRepository moduloRepository) {
        this.moduloRepository = moduloRepository;
    }

    @Override
    public void execute(final ActualizarModuloDomain data) {
        final var id = data.getId();
        if (UUIDHelper.getDefault().equals(id)) {
            throw ValidationException.build(
                    "El id del modulo es obligatorio para actualizar.");
        }

        if (moduloRepository.findById(id).isEmpty()) {
            throw NotFoundException.build(
                    "No existe un modulo con el id especificado.");
        }
    }
}
