package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

class CrearFuncionalidadDtoInputTest {

    @Test
    void debeCrearConValoresPorDefectoCuandoSeUsaElConstructorSinArgumentos() {
        var dto = new CrearFuncionalidadDtoInput();

        assertEquals("", dto.getNombre());
        assertEquals(UUIDHelper.getDefault(), dto.getIdModulo());
        assertFalse(dto.isActivo());
        assertNull(dto.getFechaInicio());
        assertNull(dto.getFechaFinal());
    }

    @Test
    void debeAplicarTrimAlNombreCuandoSeAsignaUnoConEspacios() {
        var dto = new CrearFuncionalidadDtoInput();
        dto.setNombre("  funcionalidad  ");

        assertEquals("funcionalidad", dto.getNombre());
    }

    @Test
    void debeAsignarIdPorDefectoCuandoElIdModuloEsNulo() {
        var dto = new CrearFuncionalidadDtoInput();
        dto.setIdModulo(null);

        assertEquals(UUIDHelper.getDefault(), dto.getIdModulo());
    }

    @Test
    void debeAsignarLosValoresCuandoSeUsaCreate() {
        var idModulo = UUID.randomUUID();
        var fechaInicio = LocalDateTime.now();
        var fechaFinal = fechaInicio.plusDays(1);

        var dto = CrearFuncionalidadDtoInput.create("funcionalidad", idModulo, true, fechaInicio, fechaFinal);

        assertEquals("funcionalidad", dto.getNombre());
        assertEquals(idModulo, dto.getIdModulo());
        assertTrue(dto.isActivo());
        assertEquals(fechaInicio, dto.getFechaInicio());
        assertEquals(fechaFinal, dto.getFechaFinal());
    }
}