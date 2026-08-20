package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.crearaplicacionimpl;

import static org.mockito.Mockito.inOrder;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.CrearAplicacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.AplicacionNombreDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.AplicacionNombreIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.AplicacionNombreIsNotNullRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.AplicacionOrganizacionExistsRule;

@ExtendWith(MockitoExtension.class)
class CrearAplicacionRuleValidatorImplTest {

    @Mock
    private AplicacionNombreIsNotNullRule aplicacionNombreIsNotNullRule;
    @Mock
    private AplicacionNombreIsNotEmptyRule aplicacionNombreIsNotEmptyRule;
    @Mock
    private AplicacionNombreDoesNotExistRule aplicacionNombreDoesNotExistRule;
    @Mock
    private AplicacionOrganizacionExistsRule aplicacionOrganizacionExistsRule;

    @InjectMocks
    private CrearAplicacionRuleValidatorImpl validator;

    private CrearAplicacionDomain domainValido() {
        return CrearAplicacionDomain.create(UUID.randomUUID(), "aplicacion", UUID.randomUUID(), true, null, null);
    }

    @Test
    void debeEjecutarTodasLasReglasEnOrden() {
        var domain = domainValido();

        validator.validate(domain);

        InOrder inOrder = inOrder(aplicacionNombreIsNotNullRule, aplicacionNombreIsNotEmptyRule,
                aplicacionNombreDoesNotExistRule, aplicacionOrganizacionExistsRule);
        inOrder.verify(aplicacionNombreIsNotNullRule).execute(domain);
        inOrder.verify(aplicacionNombreIsNotEmptyRule).execute(domain);
        inOrder.verify(aplicacionNombreDoesNotExistRule).execute(domain);
        inOrder.verify(aplicacionOrganizacionExistsRule).execute(domain);
    }
}