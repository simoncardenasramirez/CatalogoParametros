package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.CrearAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.exception.AplicacionException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.AplicacionNombreDoesNotExistRule;

@Service
public final class AplicacionNombreDoesNotExistRuleImpl implements AplicacionNombreDoesNotExistRule {

    private final AplicacionRepository aplicacionRepository;

    public AplicacionNombreDoesNotExistRuleImpl(final AplicacionRepository aplicacionRepository) {
        this.aplicacionRepository = aplicacionRepository;
    }

    @Override
    public void execute(final CrearAplicacionDomain data) {
        if (aplicacionRepository.existsByNombre(data.getNombre())) {
            throw new AplicacionException("Ya existe una aplicacion con el nombre " + data.getNombre() + ".");
        }
    }
}
