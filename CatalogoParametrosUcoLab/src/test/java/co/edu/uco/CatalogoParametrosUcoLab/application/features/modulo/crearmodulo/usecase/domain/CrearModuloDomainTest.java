package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

class CrearModuloDomainTest {

    private static final String UUID_DEFAULT = "00000000-0000-0000-0000-000000000000";

    @Test
    void debeCrearConLosValoresCuandoLosDatosSonValidos() {
        var id = UUID.randomUUID();
        var idAplicacion = UUID.randomUUID();
        var fechaInicio = LocalDateTime.of(2026, 1, 1, 0, 0, 0);
        var fechaFinal = LocalDateTime.of(2026, 12, 31, 23, 59, 59);

        var domain = CrearModuloDomain.create(id, "modulo", idAplicacion, true, fechaInicio, fechaFinal);

        assertEquals(id, domain.getId());
        assertEquals("modulo", domain.getNombre());
        assertEquals(idAplicacion, domain.getIdAplicacion());
        assertTrue(domain.isActivo());
        assertEquals(fechaInicio, domain.getFechaInicio());
        assertEquals(fechaFinal, domain.getFechaFinal());
    }

    @Test
    void debeRecortarElNombreCuandoTieneEspaciosAlInicioYAlFinal() {
        var domain = CrearModuloDomain.create(UUID.randomUUID(), "  modulo  ", UUID.randomUUID(), true, null, null);

        assertEquals("modulo", domain.getNombre());
    }

    @Test
    void debeAsignarElUuidPorDefectoCuandoElIdEsNulo() {
        var domain = CrearModuloDomain.create(null, "modulo", UUID.randomUUID(), true, null, null);

        assertEquals(UUID.fromString(UUID_DEFAULT), domain.getId());
    }

    @Test
    void debeAsignarElUuidPorDefectoCuandoElIdAplicacionEsNulo() {
        var domain = CrearModuloDomain.create(UUID.randomUUID(), "modulo", null, true, null, null);

        assertEquals(UUID.fromString(UUID_DEFAULT), domain.getIdAplicacion());
    }

    @Test
    void debeGenerarUnIdNoNuloCuandoSeInvocaGenerateId() {
        var domain = CrearModuloDomain.create(null, "modulo", UUID.randomUUID(), true, null, null);

        domain.generateId();

        assertNotNull(domain.getId());
        assertNotEquals(UUIDHelper.getDefault(), domain.getId());
    }
}