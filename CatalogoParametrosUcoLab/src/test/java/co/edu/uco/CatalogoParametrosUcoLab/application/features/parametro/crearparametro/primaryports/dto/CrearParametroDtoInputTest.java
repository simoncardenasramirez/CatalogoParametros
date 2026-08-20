package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

class CrearParametroDtoInputTest {

    @Test
    void debeCrearConValoresPorDefectoCuandoSeUsaElConstructorSinArgumentos() {
        var dto = new CrearParametroDtoInput();

        assertEquals("", dto.getNombre());
        assertEquals(UUIDHelper.getDefault(), dto.getIdFuncionalidad());
        assertEquals(UUIDHelper.getDefault(), dto.getIdTipoParametro());
        assertFalse(dto.isActivo());
    }

    @Test
    void debeAsignarIdPorDefectoCuandoElIdFuncionalidadEsNulo() {
        var dto = CrearParametroDtoInput.create("parametro", null, UUID.randomUUID(), true);

        assertEquals(UUIDHelper.getDefault(), dto.getIdFuncionalidad());
    }

    @Test
    void debeAsignarIdPorDefectoCuandoElIdTipoParametroEsNulo() {
        var dto = CrearParametroDtoInput.create("parametro", UUID.randomUUID(), null, true);

        assertEquals(UUIDHelper.getDefault(), dto.getIdTipoParametro());
    }

    @Test
    void debeConservarLosValoresCuandoSeCreaConDatosReales() {
        var idFuncionalidad = UUID.randomUUID();
        var idTipoParametro = UUID.randomUUID();
        var dto = CrearParametroDtoInput.create("parametro", idFuncionalidad, idTipoParametro, true);

        assertEquals("parametro", dto.getNombre());
        assertEquals(idFuncionalidad, dto.getIdFuncionalidad());
        assertEquals(idTipoParametro, dto.getIdTipoParametro());
        assertTrue(dto.isActivo());
    }

    @Test
    void debeConservarElValorActivoCuandoEsFalso() {
        var dto = CrearParametroDtoInput.create("parametro", UUID.randomUUID(), UUID.randomUUID(), false);

        assertFalse(dto.isActivo());
    }
}