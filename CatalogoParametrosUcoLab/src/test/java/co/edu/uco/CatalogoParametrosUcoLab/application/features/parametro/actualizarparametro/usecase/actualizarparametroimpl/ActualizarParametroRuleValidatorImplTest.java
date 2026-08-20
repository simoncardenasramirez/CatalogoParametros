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
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroNombreDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroNombreIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroNombreIsNotNullRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.rules.ActualizarParametroTipoParametroIsValidRule;

@ExtendWith(MockitoExtension.class)
class ActualizarParametroRuleValidatorImplTest {

    @Mock
    private ActualizarParametroNombreIsNotNullRule parametroNombreIsNotNullRule;
    @Mock
    private ActualizarParametroNombreIsNotEmptyRule parametroNombreIsNotEmptyRule;
    @Mock
    private ActualizarParametroFuncionalidadIsValidRule parametroFuncionalidadIsValidRule;
    @Mock
    private ActualizarParametroFuncionalidadExistsRule parametroFuncionalidadExistsRule;
    @Mock
    private ActualizarParametroTipoParametroIsValidRule parametroTipoParametroIsValidRule;
    @Mock
    private ActualizarParametroNombreDoesNotExistRule parametroNombreDoesNotExistRule;

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

        InOrder inOrder = inOrder(parametroNombreIsNotNullRule, parametroNombreIsNotEmptyRule,
                parametroFuncionalidadIsValidRule, parametroFuncionalidadExistsRule,
                parametroTipoParametroIsValidRule, parametroNombreDoesNotExistRule);
        inOrder.verify(parametroNombreIsNotNullRule).execute(domain);
        inOrder.verify(parametroNombreIsNotEmptyRule).execute(domain);
        inOrder.verify(parametroFuncionalidadIsValidRule).execute(domain);
        inOrder.verify(parametroFuncionalidadExistsRule).execute(domain);
        inOrder.verify(parametroTipoParametroIsValidRule).execute(domain);
        inOrder.verify(parametroNombreDoesNotExistRule).execute(domain);
    }
}