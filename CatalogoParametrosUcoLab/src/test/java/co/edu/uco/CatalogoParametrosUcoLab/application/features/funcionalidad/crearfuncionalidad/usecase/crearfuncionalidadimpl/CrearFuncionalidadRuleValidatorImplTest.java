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
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.FuncionalidadNombreDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.FuncionalidadNombreIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.usecase.domain.rules.FuncionalidadNombreIsNotNullRule;

@ExtendWith(MockitoExtension.class)
class CrearFuncionalidadRuleValidatorImplTest {

    @Mock
    private FuncionalidadNombreIsNotNullRule funcionalidadNombreIsNotNullRule;
    @Mock
    private FuncionalidadNombreIsNotEmptyRule funcionalidadNombreIsNotEmptyRule;
    @Mock
    private FuncionalidadNombreDoesNotExistRule funcionalidadNombreDoesNotExistRule;
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

        InOrder inOrder = inOrder(funcionalidadNombreIsNotNullRule, funcionalidadNombreIsNotEmptyRule,
                funcionalidadNombreDoesNotExistRule, funcionalidadModuloExistsRule);
        inOrder.verify(funcionalidadNombreIsNotNullRule).execute(domain);
        inOrder.verify(funcionalidadNombreIsNotEmptyRule).execute(domain);
        inOrder.verify(funcionalidadNombreDoesNotExistRule).execute(domain);
        inOrder.verify(funcionalidadModuloExistsRule).execute(domain);
    }
}