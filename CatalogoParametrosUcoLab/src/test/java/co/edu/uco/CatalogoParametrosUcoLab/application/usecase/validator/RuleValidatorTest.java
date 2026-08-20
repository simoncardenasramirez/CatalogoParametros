package co.edu.uco.CatalogoParametrosUcoLab.application.usecase.validator;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.BusinessException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

class RuleValidatorTest {

    private RuleValidator<String> validatorQuePasa() {
        return data -> {
            // validacion sin errores
        };
    }

    private RuleValidator<String> validatorQueFalla() {
        return data -> {
            throw ValidationException.build("El dato no es valido.");
        };
    }

    @Test
    void debeEjecutarLaValidacionSinLanzarExcepcionesCuandoLosDatosSonValidos() {
        var validator = validatorQuePasa();
        assertDoesNotThrow(() -> validator.validate("dato valido"));
    }

    @Test
    void debeLanzarBusinessExceptionCuandoLaValidacionFalla() {
        var validator = validatorQueFalla();
        assertThrows(BusinessException.class, () -> validator.validate("dato invalido"));
    }

    @Test
    void debeLanzarValidationExceptionCuandoLaValidacionFalla() {
        var validator = validatorQueFalla();
        assertThrows(ValidationException.class, () -> validator.validate("dato invalido"));
    }

    @Test
    void debeSerUnaInterfazFuncionalConUnUnicoMetodoValidate() {
        RuleValidator<String> validator = data -> assertTrue(true);
        assertDoesNotThrow(() -> validator.validate("dato"));
    }
}