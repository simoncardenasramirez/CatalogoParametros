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
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.ActualizarAplicacionNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.ActualizarAplicacionNameIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.ActualizarAplicacionNameIsNotNullRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain.rules.ActualizarAplicacionOrganizacionExistsRule;

@ExtendWith(MockitoExtension.class)
class ActualizarAplicacionRuleValidatorImplTest {

    @Mock
    private ActualizarAplicacionNameIsNotNullRule aplicacionNameIsNotNullRule;
    @Mock
    private ActualizarAplicacionNameIsNotEmptyRule aplicacionNameIsNotEmptyRule;
    @Mock
    private ActualizarAplicacionNameDoesNotExistRule aplicacionNameDoesNotExistRule;
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

        InOrder inOrder = inOrder(aplicacionNameIsNotNullRule, aplicacionNameIsNotEmptyRule,
                aplicacionNameDoesNotExistRule, aplicacionOrganizacionExistsRule, aplicacionIdExistsRule);
        inOrder.verify(aplicacionNameIsNotNullRule).execute(domain);
        inOrder.verify(aplicacionNameIsNotEmptyRule).execute(domain);
        inOrder.verify(aplicacionNameDoesNotExistRule).execute(domain);
        inOrder.verify(aplicacionOrganizacionExistsRule).execute(domain);
        inOrder.verify(aplicacionIdExistsRule).execute(domain);
    }
}