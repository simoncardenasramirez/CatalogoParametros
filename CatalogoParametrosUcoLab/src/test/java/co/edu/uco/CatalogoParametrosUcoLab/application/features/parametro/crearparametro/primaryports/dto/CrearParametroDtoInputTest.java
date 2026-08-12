package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CrearParametroDtoInputTest {

    private static final String VALID_NOMBRE = "ParametroTest";
    private static final UUID VALID_ID_FUNCIONALIDAD = UUID.randomUUID();
    private static final UUID VALID_ID_TIPO_PARAMETRO = UUID.randomUUID();
    private static final boolean VALID_ACTIVO = true;

    // ==================== TESTS DE CREACION ====================

    @Test
    void shouldCreateDtoInputWithValidData() {
        var dtoInput = new CrearParametroDtoInput(VALID_NOMBRE, VALID_ID_FUNCIONALIDAD, VALID_ID_TIPO_PARAMETRO, VALID_ACTIVO);

        assertEquals(VALID_NOMBRE, dtoInput.getNombre());
        assertEquals(VALID_ID_FUNCIONALIDAD, dtoInput.getIdFuncionalidad());
        assertEquals(VALID_ID_TIPO_PARAMETRO, dtoInput.getIdTipoParametro());
        assertEquals(VALID_ACTIVO, dtoInput.isActivo());
    }

    @Test
    void shouldCreateDtoInputWithDefaultValues() {
        var dtoInput = new CrearParametroDtoInput();

        assertEquals("", dtoInput.getNombre());
        assertNotNull(dtoInput.getIdFuncionalidad());
        assertNotNull(dtoInput.getIdTipoParametro());
        assertFalse(dtoInput.isActivo());
    }

    @Test
    void shouldCreateDtoInputUsingStaticFactory() {
        var dtoInput = CrearParametroDtoInput.create(VALID_NOMBRE, VALID_ID_FUNCIONALIDAD, VALID_ID_TIPO_PARAMETRO, VALID_ACTIVO);

        assertEquals(VALID_NOMBRE, dtoInput.getNombre());
        assertEquals(VALID_ID_FUNCIONALIDAD, dtoInput.getIdFuncionalidad());
        assertEquals(VALID_ID_TIPO_PARAMETRO, dtoInput.getIdTipoParametro());
        assertEquals(VALID_ACTIVO, dtoInput.isActivo());
    }

    // ==================== TESTS DE SETTERS ====================

    @Test
    void shouldUpdateNombreThroughSetter() {
        var dtoInput = new CrearParametroDtoInput();
        dtoInput.setNombre(VALID_NOMBRE);

        assertEquals(VALID_NOMBRE, dtoInput.getNombre());
    }

    @Test
    void shouldUpdateIdFuncionalidadThroughSetter() {
        var dtoInput = new CrearParametroDtoInput();
        dtoInput.setIdFuncionalidad(VALID_ID_FUNCIONALIDAD);

        assertEquals(VALID_ID_FUNCIONALIDAD, dtoInput.getIdFuncionalidad());
    }

    @Test
    void shouldUpdateIdTipoParametroThroughSetter() {
        var dtoInput = new CrearParametroDtoInput();
        dtoInput.setIdTipoParametro(VALID_ID_TIPO_PARAMETRO);

        assertEquals(VALID_ID_TIPO_PARAMETRO, dtoInput.getIdTipoParametro());
    }

    @Test
    void shouldUpdateActivoThroughSetter() {
        var dtoInput = new CrearParametroDtoInput();
        dtoInput.setActivo(true);

        assertTrue(dtoInput.isActivo());
    }

    @Test
    void shouldSetActivoToFalse() {
        var dtoInput = new CrearParametroDtoInput();
        dtoInput.setActivo(false);

        assertFalse(dtoInput.isActivo());
    }

    // ==================== TESTS DE VALORES POR DEFECTO ====================

    @Test
    void shouldHandleNullIdFuncionalidad() {
        var dtoInput = new CrearParametroDtoInput(VALID_NOMBRE, null, VALID_ID_TIPO_PARAMETRO, VALID_ACTIVO);

        assertNotNull(dtoInput.getIdFuncionalidad());
    }

    @Test
    void shouldHandleNullIdTipoParametro() {
        var dtoInput = new CrearParametroDtoInput(VALID_NOMBRE, VALID_ID_FUNCIONALIDAD, null, VALID_ACTIVO);

        assertNotNull(dtoInput.getIdTipoParametro());
    }

    @Test
    void shouldHandleNullNombre() {
        var dtoInput = new CrearParametroDtoInput(null, VALID_ID_FUNCIONALIDAD, VALID_ID_TIPO_PARAMETRO, VALID_ACTIVO);

        assertNull(dtoInput.getNombre());
    }
}
