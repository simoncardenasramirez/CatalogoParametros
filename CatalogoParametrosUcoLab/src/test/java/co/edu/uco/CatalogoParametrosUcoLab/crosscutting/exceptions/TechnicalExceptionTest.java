package co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class TechnicalExceptionTest {

    private static final String MENSAJE = "mensaje tecnico";

    @Test
    void debeSerUnaBusinessException() {
        assertTrue(BusinessException.class.isAssignableFrom(TechnicalException.class));
    }

    @Test
    void debeConstruirConElMensajeCuandoSeUsaBuild() {
        var excepcion = TechnicalException.build(MENSAJE);
        assertEquals(MENSAJE, excepcion.getMessage());
        assertTrue(excepcion instanceof TechnicalException);
    }

    @Test
    void debeConservarElMensajeCuandoSeUsaElConstructorConMensaje() {
        var excepcion = new TechnicalException(MENSAJE);
        assertEquals(MENSAJE, excepcion.getMessage());
    }

    @Test
    void debeConservarElMensajeYLaCausaCuandoSeUsaElConstructorConAmbos() {
        var causa = new IllegalStateException("causa raiz");
        var excepcion = new TechnicalException(MENSAJE, causa);
        assertEquals(MENSAJE, excepcion.getMessage());
        assertSame(causa, excepcion.getCause());
    }
}