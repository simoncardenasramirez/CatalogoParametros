package co.edu.uco.CatalogoParametrosUcoLab.application.features.cambiarestado.primaryports.dto;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class CambiarEstadoDtoRequestTest {

    @Test
    void debeCrearSolicitudVaciaYPermitirAsignarEstado() {
        var request = new CambiarEstadoDtoRequest();
        assertNull(request.getActivo());
        request.setActivo(false);
        assertFalse(request.getActivo());
    }

    @Test
    void debeCrearSolicitudConEstado() {
        assertFalse(new CambiarEstadoDtoRequest(false).getActivo());
    }
}
