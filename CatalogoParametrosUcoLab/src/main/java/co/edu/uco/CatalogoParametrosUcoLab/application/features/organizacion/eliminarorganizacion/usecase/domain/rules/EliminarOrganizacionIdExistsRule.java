package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.usecase.domain.rules;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.OrganizacionRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.rule.DomainRuleWithRepository;

public interface EliminarOrganizacionIdExistsRule extends DomainRuleWithRepository<UUID, OrganizacionRepository> {
}
