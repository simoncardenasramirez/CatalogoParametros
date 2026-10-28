package co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.usecase.impl;

import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.AdministrarCatalogoAplicacionRuleValidator;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.usecase.domain.AdministrarCatalogoAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.usecase.domain.rules.RecursoPerteneceAplicacionRule;

@Service
public final class AdministrarCatalogoAplicacionRuleValidatorImpl implements AdministrarCatalogoAplicacionRuleValidator {
    private final RecursoPerteneceAplicacionRule regla;
    public AdministrarCatalogoAplicacionRuleValidatorImpl(final RecursoPerteneceAplicacionRule regla) { this.regla = regla; }
    @Override public void validate(final AdministrarCatalogoAplicacionDomain datos) { regla.execute(datos); }
}
