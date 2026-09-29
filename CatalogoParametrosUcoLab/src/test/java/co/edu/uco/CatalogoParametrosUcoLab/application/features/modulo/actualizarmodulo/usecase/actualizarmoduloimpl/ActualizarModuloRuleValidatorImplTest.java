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
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.ActualizarModuloNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.ActualizarModuloNameIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.rules.ActualizarModuloNameIsNotNullRule;

@ExtendWith(MockitoExtension.class)
class ActualizarModuloRuleValidatorImplTest {

    @Mock
    private ActualizarModuloNameIsNotNullRule moduloNameIsNotNullRule;

    @Mock
    private ActualizarModuloNameIsNotEmptyRule moduloNameIsNotEmptyRule;

    @Mock
    private ActualizarModuloNameDoesNotExistRule moduloNameDoesNotExistRule;

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

        InOrder inOrder = inOrder(moduloNameIsNotNullRule, moduloNameIsNotEmptyRule,
                moduloNameDoesNotExistRule, moduloAplicacionExistsRule, moduloIdExistsRule);
        inOrder.verify(moduloNameIsNotNullRule).execute(domain);
        inOrder.verify(moduloNameIsNotEmptyRule).execute(domain);
        inOrder.verify(moduloNameDoesNotExistRule).execute(domain);
        inOrder.verify(moduloAplicacionExistsRule).execute(domain);
        inOrder.verify(moduloIdExistsRule).execute(domain);
    }
}