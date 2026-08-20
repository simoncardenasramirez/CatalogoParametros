package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.actualizarfuncionalidadimpl;

import static org.mockito.Mockito.inOrder;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.ActualizarFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.ActualizarFuncionalidadIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.ActualizarFuncionalidadModuloExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.ActualizarFuncionalidadNombreDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.ActualizarFuncionalidadNombreIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.ActualizarFuncionalidadNombreIsNotNullRule;

@ExtendWith(MockitoExtension.class)
class ActualizarFuncionalidadRuleValidatorImplTest {

    @Mock
    private ActualizarFuncionalidadNombreIsNotNullRule funcionalidadNombreIsNotNullRule;
    @Mock
    private ActualizarFuncionalidadNombreIsNotEmptyRule funcionalidadNombreIsNotEmptyRule;
    @Mock
    private ActualizarFuncionalidadNombreDoesNotExistRule funcionalidadNombreDoesNotExistRule;
    @Mock
    private ActualizarFuncionalidadModuloExistsRule funcionalidadModuloExistsRule;
    @Mock
    private ActualizarFuncionalidadIdExistsRule funcionalidadIdExistsRule;

    @InjectMocks
    private ActualizarFuncionalidadRuleValidatorImpl validator;

    private ActualizarFuncionalidadDomain domainValido() {
        return ActualizarFuncionalidadDomain.create(UUID.randomUUID(), "funcionalidad", UUID.randomUUID(), true, null,
                null);
    }

    @Test
    void debeEjecutarTodasLasReglasEnOrden() {
        var domain = domainValido();

        validator.validate(domain);

        InOrder inOrder = inOrder(funcionalidadNombreIsNotNullRule, funcionalidadNombreIsNotEmptyRule,
                funcionalidadNombreDoesNotExistRule, funcionalidadModuloExistsRule, funcionalidadIdExistsRule);
        inOrder.verify(funcionalidadNombreIsNotNullRule).execute(domain);
        inOrder.verify(funcionalidadNombreIsNotEmptyRule).execute(domain);
        inOrder.verify(funcionalidadNombreDoesNotExistRule).execute(domain);
        inOrder.verify(funcionalidadModuloExistsRule).execute(domain);
        inOrder.verify(funcionalidadIdExistsRule).execute(domain);
    }
}