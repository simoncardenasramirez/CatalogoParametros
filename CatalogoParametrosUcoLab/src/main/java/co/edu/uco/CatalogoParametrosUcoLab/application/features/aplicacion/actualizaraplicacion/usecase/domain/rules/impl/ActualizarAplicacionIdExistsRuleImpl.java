package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.ActualizarAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.ActualizarAplicacionIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@Service
public final class ActualizarAplicacionIdExistsRuleImpl implements ActualizarAplicacionIdExistsRule {

    private final AplicacionRepository aplicacionRepository;

    public ActualizarAplicacionIdExistsRuleImpl(final AplicacionRepository aplicacionRepository) {
        this.aplicacionRepository = aplicacionRepository;
    }

    @Override
    public void execute(final ActualizarAplicacionDomain data) {
        if (data == null || UUIDHelper.getDefault().equals(data.getId())) {
            throw new co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.exception.AplicacionException(
                    "El id de la aplicacion es obligatorio para actualizar.");
        }

        if (aplicacionRepository.findById(data.getId()).isEmpty()) {
            throw new co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.exception.AplicacionException(
                    "No existe una aplicacion con el id especificado.");
        }
    }
}
