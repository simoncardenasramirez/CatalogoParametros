package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.crearorganizacionimpl;

import static org.mockito.Mockito.inOrder;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.CrearOrganizacionDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.rules.OrganizacionNombreDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.rules.OrganizacionNombreIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain.rules.OrganizacionNombreIsNotNullRule;

@ExtendWith(MockitoExtension.class)
class CrearOrganizacionRuleValidatorImplTest {

    @Mock
    private OrganizacionNombreIsNotNullRule organizacionNombreIsNotNullRule;

    @Mock
    private OrganizacionNombreIsNotEmptyRule organizacionNombreIsNotEmptyRule;

    @Mock
    private OrganizacionNombreDoesNotExistRule organizacionNombreDoesNotExistRule;

    @InjectMocks
    private CrearOrganizacionRuleValidatorImpl validator;

    @Test
    void debeEjecutarTodasLasReglasEnOrden() {
        var domain = CrearOrganizacionDomain.create(UUID.randomUUID(), "organizacion");

        validator.validate(domain);

        var inOrder = inOrder(organizacionNombreIsNotNullRule, organizacionNombreIsNotEmptyRule,
                organizacionNombreDoesNotExistRule);
        inOrder.verify(organizacionNombreIsNotNullRule).execute(domain);
        inOrder.verify(organizacionNombreIsNotEmptyRule).execute(domain);
        inOrder.verify(organizacionNombreDoesNotExistRule).execute(domain);
    }
}