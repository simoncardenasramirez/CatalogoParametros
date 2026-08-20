package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

class CrearAplicacionDtoInputTest {

    @Test
    void debeCrearConValoresPorDefectoCuandoSeUsaElConstructorSinArgumentos() {
        var dto = new CrearAplicacionDtoInput();

        assertEquals("", dto.getNombre());
        assertEquals(UUIDHelper.getDefault(), dto.getIdOrganizacion());
        assertFalse(dto.isActiva());
        assertNull(dto.getFechaInicio());
        assertNull(dto.getFechaFinal());
    }

    @Test
    void debeAplicarTrimAlNombreCuandoSeAsignaUnoConEspacios() {
        var dto = new CrearAplicacionDtoInput();
        dto.setNombre("  aplicacion  ");

        assertEquals("aplicacion", dto.getNombre());
    }

    @Test
    void debeAsignarIdPorDefectoCuandoElIdOrganizacionEsNulo() {
        var dto = new CrearAplicacionDtoInput();
        dto.setIdOrganizacion(null);

        assertEquals(UUIDHelper.getDefault(), dto.getIdOrganizacion());
    }

    @Test
    void debeAsignarLosValoresCuandoSeUsaCreate() {
        var idOrganizacion = UUID.randomUUID();
        var fechaInicio = LocalDateTime.now();
        var fechaFinal = fechaInicio.plusDays(1);

        var dto = CrearAplicacionDtoInput.create("aplicacion", idOrganizacion, true, fechaInicio, fechaFinal);

        assertEquals("aplicacion", dto.getNombre());
        assertEquals(idOrganizacion, dto.getIdOrganizacion());
        assertTrue(dto.isActiva());
        assertEquals(fechaInicio, dto.getFechaInicio());
        assertEquals(fechaFinal, dto.getFechaFinal());
    }
}