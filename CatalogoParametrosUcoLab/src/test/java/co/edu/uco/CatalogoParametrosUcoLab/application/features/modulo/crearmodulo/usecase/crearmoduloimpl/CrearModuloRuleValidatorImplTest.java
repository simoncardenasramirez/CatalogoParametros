package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.crearmoduloimpl;

import static org.mockito.Mockito.inOrder;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.CrearModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.ModuloAplicacionExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.ModuloNombreDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.ModuloNombreIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.ModuloNombreIsNotNullRule;

@ExtendWith(MockitoExtension.class)
class CrearModuloRuleValidatorImplTest {

    @Mock
    private ModuloNombreIsNotNullRule moduloNombreIsNotNullRule;

    @Mock
    private ModuloNombreIsNotEmptyRule moduloNombreIsNotEmptyRule;

    @Mock
    private ModuloNombreDoesNotExistRule moduloNombreDoesNotExistRule;

    @Mock
    private ModuloAplicacionExistsRule moduloAplicacionExistsRule;

    @InjectMocks
    private CrearModuloRuleValidatorImpl validator;

    private CrearModuloDomain domainValido() {
        return CrearModuloDomain.create(UUID.randomUUID(), "modulo", UUID.randomUUID(), true, null, null);
    }

    @Test
    void debeEjecutarTodasLasReglasEnOrden() {
        var domain = domainValido();

        validator.validate(domain);

        InOrder inOrder = inOrder(moduloNombreIsNotNullRule, moduloNombreIsNotEmptyRule,
                moduloNombreDoesNotExistRule, moduloAplicacionExistsRule);
        inOrder.verify(moduloNombreIsNotNullRule).execute(domain);
        inOrder.verify(moduloNombreIsNotEmptyRule).execute(domain);
        inOrder.verify(moduloNombreDoesNotExistRule).execute(domain);
        inOrder.verify(moduloAplicacionExistsRule).execute(domain);
    }
}