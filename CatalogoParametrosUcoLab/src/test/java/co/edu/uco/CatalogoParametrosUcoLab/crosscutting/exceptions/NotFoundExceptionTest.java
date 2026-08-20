package co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class NotFoundExceptionTest {

    private static final String MENSAJE = "mensaje de no encontrado";

    @Test
    void debeSerUnaBusinessException() {
        assertTrue(BusinessException.class.isAssignableFrom(NotFoundException.class));
    }

    @Test
    void debeConstruirConElMensajeCuandoSeUsaBuild() {
        var excepcion = NotFoundException.build(MENSAJE);
        assertEquals(MENSAJE, excepcion.getMessage());
        assertTrue(excepcion instanceof NotFoundException);
    }

    @Test
    void debeConservarElMensajeCuandoSeUsaElConstructorConMensaje() {
        var excepcion = new NotFoundException(MENSAJE);
        assertEquals(MENSAJE, excepcion.getMessage());
    }

    @Test
    void debeConservarElMensajeYLaCausaCuandoSeUsaElConstructorConAmbos() {
        var causa = new IllegalStateException("causa raiz");
        var excepcion = new NotFoundException(MENSAJE, causa);
        assertEquals(MENSAJE, excepcion.getMessage());
        assertSame(causa, excepcion.getCause());
    }
}