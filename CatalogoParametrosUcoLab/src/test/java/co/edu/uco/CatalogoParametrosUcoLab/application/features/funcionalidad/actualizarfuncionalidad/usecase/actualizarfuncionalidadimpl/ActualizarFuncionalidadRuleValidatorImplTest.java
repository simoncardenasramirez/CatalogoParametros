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
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.ActualizarFuncionalidadNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.ActualizarFuncionalidadNameIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain.rules.ActualizarFuncionalidadNameIsNotNullRule;

@ExtendWith(MockitoExtension.class)
class ActualizarFuncionalidadRuleValidatorImplTest {

    @Mock
    private ActualizarFuncionalidadNameIsNotNullRule funcionalidadNameIsNotNullRule;
    @Mock
    private ActualizarFuncionalidadNameIsNotEmptyRule funcionalidadNameIsNotEmptyRule;
    @Mock
    private ActualizarFuncionalidadNameDoesNotExistRule funcionalidadNameDoesNotExistRule;
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

        InOrder inOrder = inOrder(funcionalidadNameIsNotNullRule, funcionalidadNameIsNotEmptyRule,
                funcionalidadNameDoesNotExistRule, funcionalidadModuloExistsRule, funcionalidadIdExistsRule);
        inOrder.verify(funcionalidadNameIsNotNullRule).execute(domain);
        inOrder.verify(funcionalidadNameIsNotEmptyRule).execute(domain);
        inOrder.verify(funcionalidadNameDoesNotExistRule).execute(domain);
        inOrder.verify(funcionalidadModuloExistsRule).execute(domain);
        inOrder.verify(funcionalidadIdExistsRule).execute(domain);
    }
}