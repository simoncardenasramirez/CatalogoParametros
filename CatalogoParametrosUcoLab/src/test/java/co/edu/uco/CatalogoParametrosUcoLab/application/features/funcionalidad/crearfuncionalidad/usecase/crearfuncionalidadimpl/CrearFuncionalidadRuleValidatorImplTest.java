package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.crearfuncionalidadimpl;

import static org.mockito.Mockito.inOrder;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.CrearFuncionalidadDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.FuncionalidadModuloExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.FuncionalidadNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.FuncionalidadNameIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.FuncionalidadNameIsNotNullRule;

@ExtendWith(MockitoExtension.class)
class CrearFuncionalidadRuleValidatorImplTest {

    @Mock
    private FuncionalidadNameIsNotNullRule funcionalidadNameIsNotNullRule;
    @Mock
    private FuncionalidadNameIsNotEmptyRule funcionalidadNameIsNotEmptyRule;
    @Mock
    private FuncionalidadNameDoesNotExistRule funcionalidadNameDoesNotExistRule;
    @Mock
    private FuncionalidadModuloExistsRule funcionalidadModuloExistsRule;

    @InjectMocks
    private CrearFuncionalidadRuleValidatorImpl validator;

    private CrearFuncionalidadDomain domainValido() {
        return CrearFuncionalidadDomain.create(UUID.randomUUID(), "funcionalidad", UUID.randomUUID(), true, null, null);
    }

    @Test
    void debeEjecutarTodasLasReglasEnOrden() {
        var domain = domainValido();

        validator.validate(domain);

        InOrder inOrder = inOrder(funcionalidadNameIsNotNullRule, funcionalidadNameIsNotEmptyRule,
                funcionalidadNameDoesNotExistRule, funcionalidadModuloExistsRule);
        inOrder.verify(funcionalidadNameIsNotNullRule).execute(domain);
        inOrder.verify(funcionalidadNameIsNotEmptyRule).execute(domain);
        inOrder.verify(funcionalidadNameDoesNotExistRule).execute(domain);
        inOrder.verify(funcionalidadModuloExistsRule).execute(domain);
    }
}