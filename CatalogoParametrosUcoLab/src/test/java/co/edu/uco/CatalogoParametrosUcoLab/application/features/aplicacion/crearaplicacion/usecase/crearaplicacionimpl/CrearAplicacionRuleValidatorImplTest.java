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
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.AplicacionNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.AplicacionNameIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.AplicacionNameIsNotNullRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.usecase.domain.rules.AplicacionOrganizacionExistsRule;

@ExtendWith(MockitoExtension.class)
class CrearAplicacionRuleValidatorImplTest {

    @Mock
    private AplicacionNameIsNotNullRule aplicacionNameIsNotNullRule;
    @Mock
    private AplicacionNameIsNotEmptyRule aplicacionNameIsNotEmptyRule;
    @Mock
    private AplicacionNameDoesNotExistRule aplicacionNameDoesNotExistRule;
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

        InOrder inOrder = inOrder(aplicacionNameIsNotNullRule, aplicacionNameIsNotEmptyRule,
                aplicacionNameDoesNotExistRule, aplicacionOrganizacionExistsRule);
        inOrder.verify(aplicacionNameIsNotNullRule).execute(domain);
        inOrder.verify(aplicacionNameIsNotEmptyRule).execute(domain);
        inOrder.verify(aplicacionNameDoesNotExistRule).execute(domain);
        inOrder.verify(aplicacionOrganizacionExistsRule).execute(domain);
    }
}