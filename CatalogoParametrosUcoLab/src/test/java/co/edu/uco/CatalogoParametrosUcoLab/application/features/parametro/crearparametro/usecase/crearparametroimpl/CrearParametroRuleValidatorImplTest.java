package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.crearparametroimpl;

import static org.mockito.Mockito.inOrder;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroFuncionalidadExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroFuncionalidadIsValidRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroNameFormatIsValidRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroNameIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroNameIsNotNullRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroNameLengthIsValidRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroTipoParametroIsValidRule;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

@ExtendWith(MockitoExtension.class)
class CrearParametroRuleValidatorImplTest {

    @Mock
    private ParametroNameIsNotNullRule parametroNameIsNotNullRule;
    @Mock
    private ParametroNameIsNotEmptyRule parametroNameIsNotEmptyRule;
    @Mock
    private ParametroNameLengthIsValidRule parametroNameLengthIsValidRule;
    @Mock
    private ParametroNameFormatIsValidRule parametroNameFormatIsValidRule;
    @Mock
    private ParametroFuncionalidadIsValidRule parametroFuncionalidadIsValidRule;
    @Mock
    private ParametroFuncionalidadExistsRule parametroFuncionalidadExistsRule;
    @Mock
    private ParametroTipoParametroIsValidRule parametroTipoParametroIsValidRule;
    @Mock
    private ParametroNameDoesNotExistRule parametroNameDoesNotExistRule;

    @InjectMocks
    private CrearParametroRuleValidatorImpl validator;

    private CrearParametroDomain domainValido() {
        return CrearParametroDomain.create(UUIDHelper.getDefault(), "parametro",
                UUID.randomUUID(), UUID.randomUUID(), true);
    }

    @Test
    void debeEjecutarTodasLasReglasEnOrden() {
        var domain = domainValido();

        validator.validate(domain);

        InOrder inOrder = inOrder(parametroNameIsNotNullRule, parametroNameIsNotEmptyRule,
                parametroNameLengthIsValidRule, parametroNameFormatIsValidRule,
                parametroFuncionalidadIsValidRule, parametroFuncionalidadExistsRule,
                parametroTipoParametroIsValidRule, parametroNameDoesNotExistRule);
        inOrder.verify(parametroNameIsNotNullRule).execute(domain);
        inOrder.verify(parametroNameIsNotEmptyRule).execute(domain);
        inOrder.verify(parametroNameLengthIsValidRule).execute(domain);
        inOrder.verify(parametroNameFormatIsValidRule).execute(domain);
        inOrder.verify(parametroFuncionalidadIsValidRule).execute(domain);
        inOrder.verify(parametroFuncionalidadExistsRule).execute(domain);
        inOrder.verify(parametroTipoParametroIsValidRule).execute(domain);
        inOrder.verify(parametroNameDoesNotExistRule).execute(domain);
    }
}