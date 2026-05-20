package co.edu.uco.CatalogoParametrosUcoLab.domain.modulo.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.domain.modulo.ModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.domain.modulo.exception.ModuloException;
import co.edu.uco.CatalogoParametrosUcoLab.domain.modulo.rules.ModuloNombreIsNotNullRule;

@Service
public final class ModuloNombreIsNotNullRuleImpl implements ModuloNombreIsNotNullRule {

    @Override
    public void execute(final ModuloDomain data) {
        if (data == null || data.getNombre() == null) {
            throw new ModuloException("El nombre del modulo es obligatorio.");
        }
    }
}