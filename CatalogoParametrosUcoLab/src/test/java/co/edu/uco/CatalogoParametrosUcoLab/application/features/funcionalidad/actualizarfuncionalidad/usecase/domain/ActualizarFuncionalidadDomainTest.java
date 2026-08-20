package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.usecase.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

class ActualizarFuncionalidadDomainTest {

    @Test
    void debeAsignarIdPorDefectoCuandoElIdEsNulo() {
        var domain = ActualizarFuncionalidadDomain.create(null, "  funcionalidad  ", UUID.randomUUID(), true, null,
                null);

        assertEquals(UUIDHelper.getDefault(), domain.getId());
    }

    @Test
    void debeAplicarTrimAlNombreCuandoSeCreaConEspacios() {
        var domain = ActualizarFuncionalidadDomain.create(UUID.randomUUID(), "  funcionalidad  ", UUID.randomUUID(),
                true, null, null);

        assertEquals("funcionalidad", domain.getNombre());
    }

    @Test
    void debeAsignarIdModuloPorDefectoCuandoEsNulo() {
        var domain = ActualizarFuncionalidadDomain.create(UUID.randomUUID(), "funcionalidad", null, true, null, null);

        assertEquals(UUIDHelper.getDefault(), domain.getIdModulo());
    }

    @Test
    void debeConservarLosDatosRealesCuandoSeCreaConValoresValidos() {
        var id = UUID.randomUUID();
        var idModulo = UUID.randomUUID();
        var fechaInicio = LocalDateTime.now();
        var fechaFinal = fechaInicio.plusDays(1);

        var domain = ActualizarFuncionalidadDomain.create(id, "funcionalidad", idModulo, false, fechaInicio,
                fechaFinal);

        assertEquals(id, domain.getId());
        assertEquals("funcionalidad", domain.getNombre());
        assertEquals(idModulo, domain.getIdModulo());
        assertFalse(domain.isActivo());
        assertEquals(fechaInicio, domain.getFechaInicio());
        assertEquals(fechaFinal, domain.getFechaFinal());
    }

    @Test
    void debeGenerarUnNuevoIdCuandoSeLlamaGenerateId() {
        var domain = ActualizarFuncionalidadDomain.create(UUIDHelper.getDefault(), "funcionalidad",
                UUID.randomUUID(), true, null, null);

        domain.generateId();

        assertNotEquals(UUIDHelper.getDefault(), domain.getId());
        assertTrue(domain.getId() != null);
    }
}