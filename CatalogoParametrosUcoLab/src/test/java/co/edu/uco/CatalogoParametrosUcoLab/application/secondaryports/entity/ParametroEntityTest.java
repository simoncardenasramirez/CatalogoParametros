package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

class ParametroEntityTest {

    @Test
    void debeAsignarIdPorDefectoCuandoElIdEsNulo() {
        var entity = ParametroEntity.create(null, "  parametro  ", UUID.randomUUID(), UUID.randomUUID(), true);

        assertEquals(UUIDHelper.getDefault(), entity.getId());
        assertEquals("parametro", entity.getNombre());
    }

    @Test
    void debeAsignarIdsPorDefectoCuandoLosIdsAsociadosSonNulos() {
        var entity = ParametroEntity.create(UUID.randomUUID(), "parametro", null, null, true);

        assertEquals(UUIDHelper.getDefault(), entity.getIdFuncionalidad());
        assertEquals(UUIDHelper.getDefault(), entity.getIdTipoParametro());
    }

    @Test
    void debeConservarLosValoresCuandoSeCreaConDatosReales() {
        var id = UUID.randomUUID();
        var idFuncionalidad = UUID.randomUUID();
        var idTipoParametro = UUID.randomUUID();
        var entity = ParametroEntity.create(id, "parametro", idFuncionalidad, idTipoParametro, false);

        assertEquals(id, entity.getId());
        assertEquals("parametro", entity.getNombre());
        assertEquals(idFuncionalidad, entity.getIdFuncionalidad());
        assertEquals(idTipoParametro, entity.getIdTipoParametro());
        assertEquals(false, entity.isActivo());
    }

    @Test
    void debeNormalizarElNombreCuandoSeUsaElSetter() {
        var entity = ParametroEntity.create(UUID.randomUUID(), "parametro", UUID.randomUUID(), UUID.randomUUID(), true);

        entity.setNombre("  nuevo nombre  ");

        assertEquals("nuevo nombre", entity.getNombre());
    }

    @Test
    void debeAsignarIdPorDefectoCuandoElSetterRecibeNulo() {
        var entity = ParametroEntity.create(UUID.randomUUID(), "parametro", UUID.randomUUID(), UUID.randomUUID(), true);

        entity.setId(null);

        assertEquals(UUIDHelper.getDefault(), entity.getId());
    }
}