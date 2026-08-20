package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class ActualizarOrganizacionDtoInputTest {

    @Test
    void debeCrearConValoresPorDefectoCuandoSeUsaElConstructorSinArgumentos() {
        var dto = new ActualizarOrganizacionDtoInput();
        assertEquals("", dto.getNombre());
    }

    @Test
    void debeConservarElNombreConEspaciosCuandoSeCrea() {
        var dto = ActualizarOrganizacionDtoInput.create("  organizacion  ");
        assertEquals("  organizacion  ", dto.getNombre());
    }

    @Test
    void debeConservarElValorNuloCuandoSeCreaConNulo() {
        var dto = ActualizarOrganizacionDtoInput.create(null);
        assertNull(dto.getNombre());
    }
}