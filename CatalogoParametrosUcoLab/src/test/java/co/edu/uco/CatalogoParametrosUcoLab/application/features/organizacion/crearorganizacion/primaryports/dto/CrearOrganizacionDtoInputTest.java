package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CrearOrganizacionDtoInputTest {

    @Test
    void debeCrearConValoresPorDefectoCuandoSeUsaElConstructorSinArgumentos() {
        var dto = new CrearOrganizacionDtoInput();
        assertEquals("", dto.getNombre());
    }

    @Test
    void debeAplicarTrimAlNombreCuandoSeCreaConEspacios() {
        var dto = CrearOrganizacionDtoInput.create("  organizacion  ");
        assertEquals("organizacion", dto.getNombre());
    }

    @Test
    void debeAsignarVacioCuandoElNombreEsNulo() {
        var dto = CrearOrganizacionDtoInput.create(null);
        assertEquals("", dto.getNombre());
    }

    @Test
    void debeAplicarTrimCuandoSeUsaElSetNombre() {
        var dto = new CrearOrganizacionDtoInput();
        dto.setNombre("  organizacion  ");
        assertEquals("organizacion", dto.getNombre());
    }
}