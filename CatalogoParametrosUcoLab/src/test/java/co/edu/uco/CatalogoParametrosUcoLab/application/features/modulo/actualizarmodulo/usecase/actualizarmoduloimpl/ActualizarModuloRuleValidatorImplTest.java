package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.actualizarmoduloimpl;

import static org.mockito.Mockito.inOrder;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.ActualizarModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.ActualizarModuloAplicacionExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.ActualizarModuloIdExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.ActualizarModuloNombreDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.ActualizarModuloNombreIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.ActualizarModuloNombreIsNotNullRule;

@ExtendWith(MockitoExtension.class)
class ActualizarModuloRuleValidatorImplTest {

    @Mock
    private ActualizarModuloNombreIsNotNullRule moduloNombreIsNotNullRule;

    @Mock
    private ActualizarModuloNombreIsNotEmptyRule moduloNombreIsNotEmptyRule;

    @Mock
    private ActualizarModuloNombreDoesNotExistRule moduloNombreDoesNotExistRule;

    @Mock
    private ActualizarModuloAplicacionExistsRule moduloAplicacionExistsRule;

    @Mock
    private ActualizarModuloIdExistsRule moduloIdExistsRule;

    @InjectMocks
    private ActualizarModuloRuleValidatorImpl validator;

    private ActualizarModuloDomain domainValido() {
        return ActualizarModuloDomain.create(UUID.randomUUID(), "modulo", UUID.randomUUID(), true, null, null);
    }

    @Test
    void debeEjecutarTodasLasReglasEnOrden() {
        var domain = domainValido();

        validator.validate(domain);

        InOrder inOrder = inOrder(moduloNombreIsNotNullRule, moduloNombreIsNotEmptyRule,
                moduloNombreDoesNotExistRule, moduloAplicacionExistsRule, moduloIdExistsRule);
        inOrder.verify(moduloNombreIsNotNullRule).execute(domain);
        inOrder.verify(moduloNombreIsNotEmptyRule).execute(domain);
        inOrder.verify(moduloNombreDoesNotExistRule).execute(domain);
        inOrder.verify(moduloAplicacionExistsRule).execute(domain);
        inOrder.verify(moduloIdExistsRule).execute(domain);
    }
}