package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.crearparametroimpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.exception.ParametroException;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroFuncionalidadExistsRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroFuncionalidadIsValidRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroNameDoesNotExistRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroNameFormatIsValidRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroNameIsNotEmptyRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroNameIsNotNullRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroNameLengthIsValidRule;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.rules.ParametroTipoParametroIsValidRule;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    // ==================== TESTS DE VALIDACION EXITOSA ====================

    @Test
    void shouldValidateSuccessfullyWhenAllRulesPass() {
        var domain = CrearParametroDomain.create(UUID.randomUUID(), "ParametroTest", UUID.randomUUID(), UUID.randomUUID(), true);

        assertDoesNotThrow(() -> validator.validate(domain));

        verify(parametroNameIsNotNullRule).execute(domain);
        verify(parametroNameIsNotEmptyRule).execute(domain);
        verify(parametroNameLengthIsValidRule).execute(domain);
        verify(parametroNameFormatIsValidRule).execute(domain);
        verify(parametroFuncionalidadIsValidRule).execute(domain);
        verify(parametroFuncionalidadExistsRule).execute(domain);
        verify(parametroTipoParametroIsValidRule).execute(domain);
        verify(parametroNameDoesNotExistRule).execute(domain);
    }

    // ==================== TESTS DE VALIDACION FALLIDA ====================

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        doThrow(new ParametroException("El nombre del parametro es obligatorio."))
                .when(parametroNameIsNotNullRule).execute(any());

        var domain = CrearParametroDomain.create(UUID.randomUUID(), null, UUID.randomUUID(), UUID.randomUUID(), true);

        var exception = assertThrows(ParametroException.class, () -> validator.validate(domain));
        assertEquals("El nombre del parametro es obligatorio.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        doThrow(new ParametroException("El nombre del parametro no puede estar vacio."))
                .when(parametroNameIsNotEmptyRule).execute(any());

        var domain = CrearParametroDomain.create(UUID.randomUUID(), "", UUID.randomUUID(), UUID.randomUUID(), true);

        var exception = assertThrows(ParametroException.class, () -> validator.validate(domain));
        assertEquals("El nombre del parametro no puede estar vacio.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNameLengthIsInvalid() {
        doThrow(new ParametroException("El nombre del parametro debe tener entre 3 y 120 caracteres."))
                .when(parametroNameLengthIsValidRule).execute(any());

        var domain = CrearParametroDomain.create(UUID.randomUUID(), "AB", UUID.randomUUID(), UUID.randomUUID(), true);

        var exception = assertThrows(ParametroException.class, () -> validator.validate(domain));
        assertEquals("El nombre del parametro debe tener entre 3 y 120 caracteres.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNameFormatIsInvalid() {
        doThrow(new ParametroException("El nombre del parametro solo puede contener letras, numeros, guion, punto y guion bajo."))
                .when(parametroNameFormatIsValidRule).execute(any());

        var domain = CrearParametroDomain.create(UUID.randomUUID(), "Nombre@Invalido", UUID.randomUUID(), UUID.randomUUID(), true);

        var exception = assertThrows(ParametroException.class, () -> validator.validate(domain));
        assertEquals("El nombre del parametro solo puede contener letras, numeros, guion, punto y guion bajo.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenFuncionalidadIsInvalid() {
        doThrow(new ParametroException("La funcionalidad asociada al parametro es obligatoria."))
                .when(parametroFuncionalidadIsValidRule).execute(any());

        var domain = CrearParametroDomain.create(UUID.randomUUID(), "ParametroTest", null, UUID.randomUUID(), true);

        var exception = assertThrows(ParametroException.class, () -> validator.validate(domain));
        assertEquals("La funcionalidad asociada al parametro es obligatoria.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenFuncionalidadDoesNotExist() {
        doThrow(new ParametroException("La funcionalidad con el id " + UUID.randomUUID() + " no existe en el sistema."))
                .when(parametroFuncionalidadExistsRule).execute(any());

        var domain = CrearParametroDomain.create(UUID.randomUUID(), "ParametroTest", UUID.randomUUID(), UUID.randomUUID(), true);

        var exception = assertThrows(ParametroException.class, () -> validator.validate(domain));
        assertTrue(exception.getMessage().contains("no existe en el sistema"));
    }

    @Test
    void shouldThrowExceptionWhenTipoParametroIsInvalid() {
        doThrow(new ParametroException("El tipo de parametro asociado es obligatorio."))
                .when(parametroTipoParametroIsValidRule).execute(any());

        var domain = CrearParametroDomain.create(UUID.randomUUID(), "ParametroTest", UUID.randomUUID(), null, true);

        var exception = assertThrows(ParametroException.class, () -> validator.validate(domain));
        assertEquals("El tipo de parametro asociado es obligatorio.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenNameAlreadyExists() {
        doThrow(new ParametroException("Ya existe un parametro con ese nombre."))
                .when(parametroNameDoesNotExistRule).execute(any());

        var domain = CrearParametroDomain.create(UUID.randomUUID(), "ParametroExistente", UUID.randomUUID(), UUID.randomUUID(), true);

        var exception = assertThrows(ParametroException.class, () -> validator.validate(domain));
        assertEquals("Ya existe un parametro con ese nombre.", exception.getMessage());
    }

    // ==================== TESTS DE ORDEN DE EJECUCION ====================

    @Test
    void shouldExecuteRulesInCorrectOrder() {
        var domain = CrearParametroDomain.create(UUID.randomUUID(), "ParametroTest", UUID.randomUUID(), UUID.randomUUID(), true);

        validator.validate(domain);

        var inOrder = inOrder(
                parametroNameIsNotNullRule,
                parametroNameIsNotEmptyRule,
                parametroNameLengthIsValidRule,
                parametroNameFormatIsValidRule,
                parametroFuncionalidadIsValidRule,
                parametroFuncionalidadExistsRule,
                parametroTipoParametroIsValidRule,
                parametroNameDoesNotExistRule
        );

        inOrder.verify(parametroNameIsNotNullRule).execute(domain);
        inOrder.verify(parametroNameIsNotEmptyRule).execute(domain);
        inOrder.verify(parametroNameLengthIsValidRule).execute(domain);
        inOrder.verify(parametroNameFormatIsValidRule).execute(domain);
        inOrder.verify(parametroFuncionalidadIsValidRule).execute(domain);
        inOrder.verify(parametroFuncionalidadExistsRule).execute(domain);
        inOrder.verify(parametroTipoParametroIsValidRule).execute(domain);
        inOrder.verify(parametroNameDoesNotExistRule).execute(domain);
    }

    // ==================== TESTS DE NOMBRES VALIDOS ====================

    @ParameterizedTest
    @ValueSource(strings = {"Parametro", "Parametro123", "parametro_test", "parametro.test", "parametro-test"})
    void shouldAcceptValidParameterNames(String nombre) {
        var domain = CrearParametroDomain.create(UUID.randomUUID(), nombre, UUID.randomUUID(), UUID.randomUUID(), true);

        assertDoesNotThrow(() -> validator.validate(domain));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Nombre@Invalido", "Nombre#Invalido", "Nombre$Invalido", "Nombre%Invalido", "Nombre&Invalido"})
    void shouldThrowExceptionForInvalidParameterNames(String nombre) {
        doThrow(new ParametroException("El nombre del parametro solo puede contener letras, numeros, guion, punto y guion bajo."))
                .when(parametroNameFormatIsValidRule).execute(any());

        var domain = CrearParametroDomain.create(UUID.randomUUID(), nombre, UUID.randomUUID(), UUID.randomUUID(), true);

        assertThrows(ParametroException.class, () -> validator.validate(domain));
    }
}
