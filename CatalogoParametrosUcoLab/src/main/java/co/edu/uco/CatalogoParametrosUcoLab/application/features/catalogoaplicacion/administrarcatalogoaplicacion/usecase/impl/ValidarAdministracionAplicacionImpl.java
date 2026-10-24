package co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.usecase.impl;

import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.common.telemetry.TelemetryService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.*;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.usecase.domain.AdministrarCatalogoAplicacionDomain;

@Service
public final class ValidarAdministracionAplicacionImpl implements ValidarAdministracionAplicacion {
    private final AdministrarCatalogoAplicacionRuleValidator validator;
    private final TelemetryService telemetry;
    public ValidarAdministracionAplicacionImpl(final AdministrarCatalogoAplicacionRuleValidator validator,
            final TelemetryService telemetry) { this.validator = validator; this.telemetry = telemetry; }
    @Override public void execute(final AdministrarCatalogoAplicacionDomain datos) {
        telemetry.recordBusinessOperation("validar-administracion-aplicacion", () -> validator.validate(datos));
    }
}
