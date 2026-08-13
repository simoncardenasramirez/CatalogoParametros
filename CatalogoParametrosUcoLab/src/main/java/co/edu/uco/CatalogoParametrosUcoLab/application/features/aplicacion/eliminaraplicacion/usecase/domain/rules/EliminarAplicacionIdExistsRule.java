package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.usecase.domain.rules;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AplicacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.rule.DomainRuleWithRepository;

public interface EliminarAplicacionIdExistsRule extends DomainRuleWithRepository<UUID, AplicacionRepository> {
}
