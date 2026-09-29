package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.crearorganizacionimpl;

import static org.mockito.Mockito.inOrder;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.CrearOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.rules.OrganizacionNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.rules.OrganizacionNameIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.rules.OrganizacionNameIsNotNullRule;

@ExtendWith(MockitoExtension.class)
class CrearOrganizacionRuleValidatorImplTest {

    @Mock
    private OrganizacionNameIsNotNullRule organizacionNameIsNotNullRule;

    @Mock
    private OrganizacionNameIsNotEmptyRule organizacionNameIsNotEmptyRule;

    @Mock
    private OrganizacionNameDoesNotExistRule organizacionNameDoesNotExistRule;

    @InjectMocks
    private CrearOrganizacionRuleValidatorImpl validator;

    @Test
    void debeEjecutarTodasLasReglasEnOrden() {
        var domain = CrearOrganizacionDomain.create(UUID.randomUUID(), "organizacion");

        validator.validate(domain);

        var inOrder = inOrder(organizacionNameIsNotNullRule, organizacionNameIsNotEmptyRule,
                organizacionNameDoesNotExistRule);
        inOrder.verify(organizacionNameIsNotNullRule).execute(domain);
        inOrder.verify(organizacionNameIsNotEmptyRule).execute(domain);
        inOrder.verify(organizacionNameDoesNotExistRule).execute(domain);
    }
}