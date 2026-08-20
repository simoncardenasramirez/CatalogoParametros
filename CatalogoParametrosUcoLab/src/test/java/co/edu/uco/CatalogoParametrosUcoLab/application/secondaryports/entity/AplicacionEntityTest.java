package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

class AplicacionEntityTest {

    @Test
    void debeAsignarIdPorDefectoCuandoElIdEsNulo() {
        var entity = AplicacionEntity.create(null, "  aplicacion  ", UUID.randomUUID(), true, null, null);

        assertEquals(UUIDHelper.getDefault(), entity.getId());
    }

    @Test
    void debeAplicarTrimAlNombreCuandoSeCreaConEspacios() {
        var entity = AplicacionEntity.create(UUID.randomUUID(), "  aplicacion  ", UUID.randomUUID(), true, null, null);

        assertEquals("aplicacion", entity.getNombre());
    }

    @Test
    void debeAsignarIdOrganizacionPorDefectoCuandoEsNulo() {
        var entity = AplicacionEntity.create(UUID.randomUUID(), "aplicacion", null, true, null, null);

        assertEquals(UUIDHelper.getDefault(), entity.getIdOrganizacion());
    }

    @Test
    void debeConservarLosDatosRealesCuandoSeCreaConValoresValidos() {
        var id = UUID.randomUUID();
        var idOrganizacion = UUID.randomUUID();
        var fechaInicio = LocalDateTime.now();
        var fechaFinal = fechaInicio.plusDays(1);

        var entity = AplicacionEntity.create(id, "aplicacion", idOrganizacion, false, fechaInicio, fechaFinal);

        assertEquals(id, entity.getId());
        assertEquals("aplicacion", entity.getNombre());
        assertEquals(idOrganizacion, entity.getIdOrganizacion());
        assertFalse(entity.isActiva());
        assertEquals(fechaInicio, entity.getFechaInicio());
        assertEquals(fechaFinal, entity.getFechaFinal());
    }

    @Test
    void debeNormalizarConLosSettersCuandoSeAsignanValoresNulos() {
        var entity = AplicacionEntity.create(UUID.randomUUID(), "aplicacion", UUID.randomUUID(), true, null, null);

        entity.setId(null);
        entity.setNombre("  otro nombre  ");
        entity.setIdOrganizacion(null);
        entity.setActiva(false);

        assertEquals(UUIDHelper.getDefault(), entity.getId());
        assertEquals("otro nombre", entity.getNombre());
        assertEquals(UUIDHelper.getDefault(), entity.getIdOrganizacion());
        assertFalse(entity.isActiva());
        assertNull(entity.getFechaInicio());
        assertNull(entity.getFechaFinal());
    }
}