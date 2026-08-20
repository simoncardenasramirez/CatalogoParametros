package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.actualizaraplicacionimpl;

import static org.mockito.Mockito.inOrder;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.ActualizarAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.ActualizarAplicacionIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.ActualizarAplicacionNombreDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.ActualizarAplicacionNombreIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.ActualizarAplicacionNombreIsNotNullRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.ActualizarAplicacionOrganizacionExistsRule;

@ExtendWith(MockitoExtension.class)
class ActualizarAplicacionRuleValidatorImplTest {

    @Mock
    private ActualizarAplicacionNombreIsNotNullRule aplicacionNombreIsNotNullRule;
    @Mock
    private ActualizarAplicacionNombreIsNotEmptyRule aplicacionNombreIsNotEmptyRule;
    @Mock
    private ActualizarAplicacionNombreDoesNotExistRule aplicacionNombreDoesNotExistRule;
    @Mock
    private ActualizarAplicacionOrganizacionExistsRule aplicacionOrganizacionExistsRule;
    @Mock
    private ActualizarAplicacionIdExistsRule aplicacionIdExistsRule;

    @InjectMocks
    private ActualizarAplicacionRuleValidatorImpl validator;

    private ActualizarAplicacionDomain domainValido() {
        return ActualizarAplicacionDomain.create(UUID.randomUUID(), "aplicacion", UUID.randomUUID(), true, null, null);
    }

    @Test
    void debeEjecutarTodasLasReglasEnOrden() {
        var domain = domainValido();

        validator.validate(domain);

        InOrder inOrder = inOrder(aplicacionNombreIsNotNullRule, aplicacionNombreIsNotEmptyRule,
                aplicacionNombreDoesNotExistRule, aplicacionOrganizacionExistsRule, aplicacionIdExistsRule);
        inOrder.verify(aplicacionNombreIsNotNullRule).execute(domain);
        inOrder.verify(aplicacionNombreIsNotEmptyRule).execute(domain);
        inOrder.verify(aplicacionNombreDoesNotExistRule).execute(domain);
        inOrder.verify(aplicacionOrganizacionExistsRule).execute(domain);
        inOrder.verify(aplicacionIdExistsRule).execute(domain);
    }
}