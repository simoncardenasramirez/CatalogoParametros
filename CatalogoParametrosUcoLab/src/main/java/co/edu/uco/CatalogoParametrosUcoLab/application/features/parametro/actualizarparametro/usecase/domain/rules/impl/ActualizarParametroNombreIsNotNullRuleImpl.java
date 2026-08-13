package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.impl;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.ActualizarParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroNombreIsNotNullRule;
import org.springframework.stereotype.Service;

@Service
public final class ActualizarParametroNombreIsNotNullRuleImpl implements ActualizarParametroNombreIsNotNullRule {

    @Override
    public void execute(final ActualizarParametroDomain data) {
        if (data == null || data.getNombre() == null) {
            throw ValidationException.build("El nombre del parametro es obligatorio.");
        }
    }
}
