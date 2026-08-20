package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class ResponseTest {

    @Test
    void debeIniciarConLaListaDeMensajesVacia() {
        var response = new Response();

        assertNotNull(response.getMensajes());
        assertTrue(response.getMensajes().isEmpty());
    }

    @Test
    void debePermitirAgregarMensajesALaListaDeMensajes() {
        var response = new Response();

        response.getMensajes().add("Primer mensaje");
        response.getMensajes().add("Segundo mensaje");

        assertEquals(List.of("Primer mensaje", "Segundo mensaje"), response.getMensajes());
    }
}