package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.actualizarparametroimpl;

import static org.mockito.Mockito.inOrder;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.ActualizarParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroFuncionalidadExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroFuncionalidadIsValidRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroNameIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroNameIsNotNullRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroTipoParametroIsValidRule;

@ExtendWith(MockitoExtension.class)
class ActualizarParametroRuleValidatorImplTest {

    @Mock
    private ActualizarParametroNameIsNotNullRule parametroNameIsNotNullRule;
    @Mock
    private ActualizarParametroNameIsNotEmptyRule parametroNameIsNotEmptyRule;
    @Mock
    private ActualizarParametroFuncionalidadIsValidRule parametroFuncionalidadIsValidRule;
    @Mock
    private ActualizarParametroFuncionalidadExistsRule parametroFuncionalidadExistsRule;
    @Mock
    private ActualizarParametroTipoParametroIsValidRule parametroTipoParametroIsValidRule;
    @Mock
    private ActualizarParametroNameDoesNotExistRule parametroNameDoesNotExistRule;

    @InjectMocks
    private ActualizarParametroRuleValidatorImpl validator;

    private ActualizarParametroDomain domainValido() {
        return ActualizarParametroDomain.create(UUID.randomUUID(), "parametro",
                UUID.randomUUID(), UUID.randomUUID(), true);
    }

    @Test
    void debeEjecutarTodasLasReglasEnOrden() {
        var domain = domainValido();

        validator.validate(domain);

        InOrder inOrder = inOrder(parametroNameIsNotNullRule, parametroNameIsNotEmptyRule,
                parametroFuncionalidadIsValidRule, parametroFuncionalidadExistsRule,
                parametroTipoParametroIsValidRule, parametroNameDoesNotExistRule);
        inOrder.verify(parametroNameIsNotNullRule).execute(domain);
        inOrder.verify(parametroNameIsNotEmptyRule).execute(domain);
        inOrder.verify(parametroFuncionalidadIsValidRule).execute(domain);
        inOrder.verify(parametroFuncionalidadExistsRule).execute(domain);
        inOrder.verify(parametroTipoParametroIsValidRule).execute(domain);
        inOrder.verify(parametroNameDoesNotExistRule).execute(domain);
    }
}