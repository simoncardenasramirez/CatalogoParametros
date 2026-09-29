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
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.ModuloNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.ModuloNameIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.rules.ModuloNameIsNotNullRule;

@ExtendWith(MockitoExtension.class)
class CrearModuloRuleValidatorImplTest {

    @Mock
    private ModuloNameIsNotNullRule moduloNameIsNotNullRule;

    @Mock
    private ModuloNameIsNotEmptyRule moduloNameIsNotEmptyRule;

    @Mock
    private ModuloNameDoesNotExistRule moduloNameDoesNotExistRule;

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

        InOrder inOrder = inOrder(moduloNameIsNotNullRule, moduloNameIsNotEmptyRule,
                moduloNameDoesNotExistRule, moduloAplicacionExistsRule);
        inOrder.verify(moduloNameIsNotNullRule).execute(domain);
        inOrder.verify(moduloNameIsNotEmptyRule).execute(domain);
        inOrder.verify(moduloNameDoesNotExistRule).execute(domain);
        inOrder.verify(moduloAplicacionExistsRule).execute(domain);
    }
}