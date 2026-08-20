package co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ConflictExceptionTest {

    private static final String MENSAJE = "mensaje de conflicto";

    @Test
    void debeSerUnaBusinessException() {
        assertTrue(BusinessException.class.isAssignableFrom(ConflictException.class));
    }

    @Test
    void debeConstruirConElMensajeCuandoSeUsaBuild() {
        var excepcion = ConflictException.build(MENSAJE);
        assertEquals(MENSAJE, excepcion.getMessage());
        assertTrue(excepcion instanceof ConflictException);
    }

    @Test
    void debeConservarElMensajeCuandoSeUsaElConstructorConMensaje() {
        var excepcion = new ConflictException(MENSAJE);
        assertEquals(MENSAJE, excepcion.getMessage());
    }

    @Test
    void debeConservarElMensajeYLaCausaCuandoSeUsaElConstructorConAmbos() {
        var causa = new IllegalStateException("causa raiz");
        var excepcion = new ConflictException(MENSAJE, causa);
        assertEquals(MENSAJE, excepcion.getMessage());
        assertSame(causa, excepcion.getCause());
    }
}