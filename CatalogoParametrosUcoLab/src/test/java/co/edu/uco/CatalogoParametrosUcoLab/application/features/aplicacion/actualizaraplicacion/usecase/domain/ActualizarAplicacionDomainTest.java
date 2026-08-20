package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.usecase.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

class ActualizarAplicacionDomainTest {

    @Test
    void debeAsignarIdPorDefectoCuandoElIdEsNulo() {
        var domain = ActualizarAplicacionDomain.create(null, "  aplicacion  ", UUID.randomUUID(), true, null, null);

        assertEquals(UUIDHelper.getDefault(), domain.getId());
    }

    @Test
    void debeAplicarTrimAlNombreCuandoSeCreaConEspacios() {
        var domain = ActualizarAplicacionDomain.create(UUID.randomUUID(), "  aplicacion  ", UUID.randomUUID(), true, null, null);

        assertEquals("aplicacion", domain.getNombre());
    }

    @Test
    void debeAsignarIdOrganizacionPorDefectoCuandoEsNulo() {
        var domain = ActualizarAplicacionDomain.create(UUID.randomUUID(), "aplicacion", null, true, null, null);

        assertEquals(UUIDHelper.getDefault(), domain.getIdOrganizacion());
    }

    @Test
    void debeConservarLosDatosRealesCuandoSeCreaConValoresValidos() {
        var id = UUID.randomUUID();
        var idOrganizacion = UUID.randomUUID();
        var fechaInicio = LocalDateTime.now();
        var fechaFinal = fechaInicio.plusDays(1);

        var domain = ActualizarAplicacionDomain.create(id, "aplicacion", idOrganizacion, false, fechaInicio, fechaFinal);

        assertEquals(id, domain.getId());
        assertEquals("aplicacion", domain.getNombre());
        assertEquals(idOrganizacion, domain.getIdOrganizacion());
        assertFalse(domain.isActiva());
        assertEquals(fechaInicio, domain.getFechaInicio());
        assertEquals(fechaFinal, domain.getFechaFinal());
    }

    @Test
    void debeGenerarUnNuevoIdCuandoSeLlamaGenerateId() {
        var domain = ActualizarAplicacionDomain.create(UUIDHelper.getDefault(), "aplicacion", UUID.randomUUID(), true, null, null);

        domain.generateId();

        assertNotEquals(UUIDHelper.getDefault(), domain.getId());
        assertTrue(domain.getId() != null);
    }
}