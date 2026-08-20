package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

class FuncionalidadEntityTest {

    @Test
    void debeNormalizarElIdCuandoEsNulo() {
        var entity = FuncionalidadEntity.create(null, "funcionalidad", UUID.randomUUID(), true, null, null);

        assertEquals(UUIDHelper.getDefault(), entity.getId());
    }

    @Test
    void debeNormalizarElIdModuloCuandoEsNulo() {
        var entity = FuncionalidadEntity.create(UUID.randomUUID(), "funcionalidad", null, true, null, null);

        assertEquals(UUIDHelper.getDefault(), entity.getIdModulo());
    }

    @Test
    void debeRecortarElNombreCuandoTieneEspaciosEnLosExtremos() {
        var entity = FuncionalidadEntity.create(UUID.randomUUID(), "  funcionalidad  ", UUID.randomUUID(), true,
                null, null);

        assertEquals("funcionalidad", entity.getNombre());
    }

    @Test
    void debeConservarLosValoresCuandoSeCreanConDatosValidos() {
        var id = UUID.randomUUID();
        var idModulo = UUID.randomUUID();
        var fechaInicio = LocalDateTime.now();
        var fechaFinal = fechaInicio.plusDays(1);

        var entity = FuncionalidadEntity.create(id, "funcionalidad", idModulo, true, fechaInicio, fechaFinal);

        assertEquals(id, entity.getId());
        assertEquals("funcionalidad", entity.getNombre());
        assertEquals(idModulo, entity.getIdModulo());
        assertTrue(entity.isActivo());
        assertEquals(fechaInicio, entity.getFechaInicio());
        assertEquals(fechaFinal, entity.getFechaFinal());
    }
}