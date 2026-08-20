package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.tipoparametro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoParametroEntity;

class TipoParametroResponseTest {

    @Test
    void debeIniciarConLasListasDeTiposParametroYMensajesVacias() {
        var response = new TipoParametroResponse();

        assertNotNull(response.getTiposParametro());
        assertTrue(response.getTiposParametro().isEmpty());
        assertNotNull(response.getMensajes());
        assertTrue(response.getMensajes().isEmpty());
    }

    @Test
    void debePermitirAgregarTiposParametroYMensajes() {
        var response = new TipoParametroResponse();
        var tipoParametro = TipoParametroEntity.create(UUID.randomUUID(), "Texto");

        response.getTiposParametro().add(tipoParametro);
        response.getMensajes().add("Mensaje de respuesta");

        assertEquals(List.of(tipoParametro), response.getTiposParametro());
        assertEquals(List.of("Mensaje de respuesta"), response.getMensajes());
    }
}