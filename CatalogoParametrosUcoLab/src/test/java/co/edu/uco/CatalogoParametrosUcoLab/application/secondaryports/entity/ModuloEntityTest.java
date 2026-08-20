package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

class ModuloEntityTest {

    @Test
    void debeCrearConLosValoresCuandoLosDatosSonValidos() {
        var id = UUID.randomUUID();
        var idAplicacion = UUID.randomUUID();
        var fechaInicio = LocalDateTime.of(2026, 1, 1, 0, 0, 0);
        var fechaFinal = LocalDateTime.of(2026, 12, 31, 23, 59, 59);

        var entity = ModuloEntity.create(id, "modulo", idAplicacion, true, fechaInicio, fechaFinal);

        assertEquals(id, entity.getId());
        assertEquals("modulo", entity.getNombre());
        assertEquals(idAplicacion, entity.getIdAplicacion());
        assertTrue(entity.isActivo());
        assertEquals(fechaInicio, entity.getFechaInicio());
        assertEquals(fechaFinal, entity.getFechaFinal());
    }

    @Test
    void debeConservarElNombreSinRecortarCuandoTieneEspacios() {
        var entity = ModuloEntity.create(UUID.randomUUID(), "  modulo  ", UUID.randomUUID(), true, null, null);

        assertEquals("  modulo  ", entity.getNombre());
    }

    @Test
    void debeConservarElIdNuloCuandoSePasaUnIdNulo() {
        var entity = ModuloEntity.create(null, "modulo", UUID.randomUUID(), true, null, null);

        assertNull(entity.getId());
    }

    @Test
    void debeConservarElIdAplicacionNuloCuandoSePasaUnIdAplicacionNulo() {
        var entity = ModuloEntity.create(UUID.randomUUID(), "modulo", null, true, null, null);

        assertNull(entity.getIdAplicacion());
    }
}