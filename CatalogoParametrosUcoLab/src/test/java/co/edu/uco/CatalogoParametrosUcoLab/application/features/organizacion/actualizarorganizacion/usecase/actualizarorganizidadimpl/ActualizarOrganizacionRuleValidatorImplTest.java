package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.actualizarorganizidadimpl;

import static org.mockito.Mockito.inOrder;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.ActualizarOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.rules.ActualizarOrganizacionIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.rules.ActualizarOrganizacionNombreDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.rules.ActualizarOrganizacionNombreIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.usecase.domain.rules.ActualizarOrganizacionNombreIsNotNullRule;

@ExtendWith(MockitoExtension.class)
class ActualizarOrganizacionRuleValidatorImplTest {

    @Mock
    private ActualizarOrganizacionIdExistsRule idExistsRule;

    @Mock
    private ActualizarOrganizacionNombreIsNotNullRule nombreIsNotNullRule;

    @Mock
    private ActualizarOrganizacionNombreIsNotEmptyRule nombreIsNotEmptyRule;

    @Mock
    private ActualizarOrganizacionNombreDoesNotExistRule nombreDoesNotExistRule;

    @InjectMocks
    private ActualizarOrganizacionRuleValidatorImpl validator;

    @Test
    void debeEjecutarTodasLasReglasEnOrden() {
        var domain = ActualizarOrganizacionDomain.create(UUID.randomUUID(), "organizacion");

        validator.validate(domain);

        var inOrder = inOrder(idExistsRule, nombreIsNotNullRule, nombreIsNotEmptyRule,
                nombreDoesNotExistRule);
        inOrder.verify(idExistsRule).execute(domain);
        inOrder.verify(nombreIsNotNullRule).execute(domain);
        inOrder.verify(nombreIsNotEmptyRule).execute(domain);
        inOrder.verify(nombreDoesNotExistRule).execute(domain);
    }
}