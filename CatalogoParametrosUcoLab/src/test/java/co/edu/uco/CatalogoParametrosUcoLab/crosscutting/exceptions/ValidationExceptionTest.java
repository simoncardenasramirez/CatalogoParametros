package co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ValidationExceptionTest {

    private static final String MENSAJE = "mensaje de validacion";

    @Test
    void debeSerUnaBusinessException() {
        assertTrue(BusinessException.class.isAssignableFrom(ValidationException.class));
    }

    @Test
    void debeConstruirConElMensajeCuandoSeUsaBuild() {
        var excepcion = ValidationException.build(MENSAJE);
        assertEquals(MENSAJE, excepcion.getMessage());
        assertTrue(excepcion instanceof ValidationException);
    }

    @Test
    void debeConservarElMensajeCuandoSeUsaElConstructorConMensaje() {
        var excepcion = new ValidationException(MENSAJE);
        assertEquals(MENSAJE, excepcion.getMessage());
    }

    @Test
    void debeConservarElMensajeYLaCausaCuandoSeUsaElConstructorConAmbos() {
        var causa = new IllegalStateException("causa raiz");
        var excepcion = new ValidationException(MENSAJE, causa);
        assertEquals(MENSAJE, excepcion.getMessage());
        assertSame(causa, excepcion.getCause());
    }
}