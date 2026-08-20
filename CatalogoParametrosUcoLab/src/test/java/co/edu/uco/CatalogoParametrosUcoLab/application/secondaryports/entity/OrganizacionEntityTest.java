package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

class OrganizacionEntityTest {

    @Test
    void debeAsignarIdPorDefectoYNombreNormalizadoCuandoSeCreaConValoresNulos() {
        var entity = OrganizacionEntity.create(null, "  organizacion  ");
        assertEquals(UUIDHelper.getDefault(), entity.getId());
        assertEquals("organizacion", entity.getNombre());
    }

    @Test
    void debeConservarElIdCuandoNoEsNulo() {
        var id = UUID.randomUUID();
        var entity = OrganizacionEntity.create(id, "organizacion");
        assertEquals(id, entity.getId());
    }

    @Test
    void debeAsignarVacioCuandoElNombreEsNulo() {
        var entity = OrganizacionEntity.create(UUID.randomUUID(), null);
        assertEquals("", entity.getNombre());
    }

    @Test
    void debeNormalizarLosValoresCuandoSeUsanLosSetters() {
        var entity = OrganizacionEntity.create(UUID.randomUUID(), "organizacion");
        entity.setId(null);
        entity.setNombre("  otra organizacion  ");
        assertEquals(UUIDHelper.getDefault(), entity.getId());
        assertEquals("otra organizacion", entity.getNombre());
    }
}