package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

class ActualizarParametroDomainTest {

    @Test
    void debeAsignarIdPorDefectoCuandoElIdEsNulo() {
        var domain = ActualizarParametroDomain.create(null, "  parametro  ", UUID.randomUUID(), UUID.randomUUID(), true);

        assertEquals(UUIDHelper.getDefault(), domain.getId());
        assertEquals("parametro", domain.getNombre());
    }

    @Test
    void debeAsignarIdsPorDefectoCuandoLosIdsAsociadosSonNulos() {
        var domain = ActualizarParametroDomain.create(UUID.randomUUID(), "parametro", null, null, true);

        assertEquals(UUIDHelper.getDefault(), domain.getIdFuncionalidad());
        assertEquals(UUIDHelper.getDefault(), domain.getIdTipoParametro());
    }

    @Test
    void debeConservarLosValoresCuandoSeCreaConDatosReales() {
        var id = UUID.randomUUID();
        var idFuncionalidad = UUID.randomUUID();
        var idTipoParametro = UUID.randomUUID();
        var domain = ActualizarParametroDomain.create(id, "parametro", idFuncionalidad, idTipoParametro, false);

        assertEquals(id, domain.getId());
        assertEquals("parametro", domain.getNombre());
        assertEquals(idFuncionalidad, domain.getIdFuncionalidad());
        assertEquals(idTipoParametro, domain.getIdTipoParametro());
        assertEquals(false, domain.isActivo());
    }

    @Test
    void debeGenerarUnIdNoNuloNiPorDefectoCuandoSeInvocaGenerateId() {
        var domain = ActualizarParametroDomain.create(null, "parametro", UUID.randomUUID(), UUID.randomUUID(), true);

        domain.generateId();

        assertNotNull(domain.getId());
        assertNotEquals(UUIDHelper.getDefault(), domain.getId());
    }
}