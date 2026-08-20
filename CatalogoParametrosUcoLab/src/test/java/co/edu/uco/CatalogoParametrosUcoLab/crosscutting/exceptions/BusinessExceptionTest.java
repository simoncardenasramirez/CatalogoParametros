package co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class BusinessExceptionTest {

    private static final String MENSAJE = "mensaje de negocio";

    private BusinessException excepcionConMensaje(final String message) {
        return new BusinessException(message) {
            private static final long serialVersionUID = 1L;
        };
    }

    private BusinessException excepcionConMensajeYCausa(final String message, final Throwable cause) {
        return new BusinessException(message, cause) {
            private static final long serialVersionUID = 1L;
        };
    }

    @Test
    void debeSerUnaExcepcionDeTipoRuntime() {
        assertTrue(RuntimeException.class.isAssignableFrom(BusinessException.class));
    }

    @Test
    void debeConservarElMensajeCuandoSeConstruyeSoloConMensaje() {
        var excepcion = excepcionConMensaje(MENSAJE);
        assertEquals(MENSAJE, excepcion.getMessage());
    }

    @Test
    void debeConservarElMensajeYLaCausaCuandoSeConstruyeConAmbos() {
        var causa = new IllegalStateException("causa raiz");
        var excepcion = excepcionConMensajeYCausa(MENSAJE, causa);
        assertEquals(MENSAJE, excepcion.getMessage());
        assertSame(causa, excepcion.getCause());
    }
}