package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.usecase.domain.rules;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.rule.DomainRule;

public interface EliminarAplicacionIsNotUsedByModuloRule extends DomainRule<UUID> {
}
