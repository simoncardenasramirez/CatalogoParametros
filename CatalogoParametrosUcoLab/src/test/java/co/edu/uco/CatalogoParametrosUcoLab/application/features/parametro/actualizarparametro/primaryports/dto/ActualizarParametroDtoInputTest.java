package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

class ActualizarParametroDtoInputTest {

    @Test
    void debeCrearConValoresPorDefectoCuandoSeUsaElConstructorSinArgumentos() {
        var dto = new ActualizarParametroDtoInput();

        assertEquals("", dto.getNombre());
        assertEquals(UUIDHelper.getDefault(), dto.getIdFuncionalidad());
        assertEquals(UUIDHelper.getDefault(), dto.getIdTipoParametro());
        assertFalse(dto.isActivo());
    }

    @Test
    void debeAsignarIdPorDefectoCuandoElIdFuncionalidadEsNulo() {
        var dto = ActualizarParametroDtoInput.create("parametro", null, UUID.randomUUID(), true);

        assertEquals(UUIDHelper.getDefault(), dto.getIdFuncionalidad());
    }

    @Test
    void debeAsignarIdPorDefectoCuandoElIdTipoParametroEsNulo() {
        var dto = ActualizarParametroDtoInput.create("parametro", UUID.randomUUID(), null, true);

        assertEquals(UUIDHelper.getDefault(), dto.getIdTipoParametro());
    }

    @Test
    void debeConservarLosValoresCuandoSeCreaConDatosReales() {
        var idFuncionalidad = UUID.randomUUID();
        var idTipoParametro = UUID.randomUUID();
        var dto = ActualizarParametroDtoInput.create("parametro", idFuncionalidad, idTipoParametro, true);

        assertEquals("parametro", dto.getNombre());
        assertEquals(idFuncionalidad, dto.getIdFuncionalidad());
        assertEquals(idTipoParametro, dto.getIdTipoParametro());
        assertTrue(dto.isActivo());
    }

    @Test
    void debeConservarElValorActivoCuandoEsFalso() {
        var dto = ActualizarParametroDtoInput.create("parametro", UUID.randomUUID(), UUID.randomUUID(), false);

        assertFalse(dto.isActivo());
    }
}