package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

class CrearModuloDtoInputTest {

    @Test
    void debeCrearConValoresPorDefectoCuandoSeUsaElConstructorSinArgumentos() {
        var dto = new CrearModuloDtoInput();

        assertEquals("", dto.getNombre());
        assertEquals(UUIDHelper.getDefault(), dto.getIdAplicacion());
        assertFalse(dto.isActivo());
        assertNull(dto.getFechaInicio());
        assertNull(dto.getFechaFinal());
    }

    @Test
    void debeCrearConLosValoresCuandoLosDatosSonValidos() {
        var idAplicacion = UUID.randomUUID();
        var fechaInicio = LocalDateTime.of(2026, 1, 1, 0, 0, 0);
        var fechaFinal = LocalDateTime.of(2026, 12, 31, 23, 59, 59);

        var dto = CrearModuloDtoInput.create("modulo", idAplicacion, true, fechaInicio, fechaFinal);

        assertEquals("modulo", dto.getNombre());
        assertEquals(idAplicacion, dto.getIdAplicacion());
        assertTrue(dto.isActivo());
        assertEquals(fechaInicio, dto.getFechaInicio());
        assertEquals(fechaFinal, dto.getFechaFinal());
    }

    @Test
    void debeAsignarElUuidPorDefectoCuandoElIdAplicacionEsNulo() {
        var dto = new CrearModuloDtoInput();
        dto.setIdAplicacion(null);

        assertEquals(UUIDHelper.getDefault(), dto.getIdAplicacion());
    }

    @Test
    void debeConservarElNombreSinRecortarCuandoTieneEspacios() {
        var dto = new CrearModuloDtoInput();
        dto.setNombre("  modulo  ");

        assertEquals("  modulo  ", dto.getNombre());
    }
}