package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.usecase.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

class CrearOrganizacionDomainTest {

    @Test
    void debeAsignarIdPorDefectoYNombreNormalizadoCuandoElIdEsNulo() {
        var domain = CrearOrganizacionDomain.create(null, "  organizacion  ");
        assertEquals(UUIDHelper.getDefault(), domain.getId());
        assertEquals("organizacion", domain.getNombre());
    }

    @Test
    void debeConservarElIdCuandoNoEsNulo() {
        var id = UUID.randomUUID();
        var domain = CrearOrganizacionDomain.create(id, "organizacion");
        assertEquals(id, domain.getId());
    }

    @Test
    void debeAsignarVacioCuandoElNombreEsNulo() {
        var domain = CrearOrganizacionDomain.create(UUID.randomUUID(), null);
        assertEquals("", domain.getNombre());
    }

    @Test
    void debeGenerarUnIdNoNuloCuandoSeInvocaGenerateId() {
        var domain = CrearOrganizacionDomain.create(UUID.randomUUID(), "organizacion");
        domain.generateId();
        assertNotNull(domain.getId());
        assertNotEquals(UUIDHelper.getDefault(), domain.getId());
    }
}