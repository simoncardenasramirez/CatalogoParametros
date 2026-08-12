package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.exception.ParametroException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CrearParametroDtoRequestTest {

    private static final String VALID_NOMBRE = "ParametroTest";
    private static final String VALID_ID_FUNCIONALIDAD = UUID.randomUUID().toString();
    private static final String VALID_ID_TIPO_PARAMETRO = UUID.randomUUID().toString();
    private static final String VALID_ACTIVO = "true";

    // ==================== TESTS DE NOMBRE ====================

    @Test
    void shouldCreateRequestWithValidData() {
        var request = new CrearParametroDtoRequest(VALID_NOMBRE, VALID_ID_FUNCIONALIDAD, VALID_ID_TIPO_PARAMETRO, VALID_ACTIVO);

        assertEquals(VALID_NOMBRE, request.getNombre());
        assertEquals(VALID_ID_FUNCIONALIDAD, request.getIdFuncionalidad());
        assertEquals(VALID_ID_TIPO_PARAMETRO, request.getIdTipoParametro());
        assertEquals(VALID_ACTIVO, request.getActivo());
    }

    @Test
    void shouldCreateRequestWithDefaultValues() {
        // El constructor por defecto lanza excepcion porque nombre vacio no es valido
        assertThrows(ParametroException.class, () -> new CrearParametroDtoRequest());
    }

    @Test
    void shouldCreateRequestUsingStaticFactory() {
        var request = CrearParametroDtoRequest.create(VALID_NOMBRE, VALID_ID_FUNCIONALIDAD, VALID_ID_TIPO_PARAMETRO, VALID_ACTIVO);

        assertEquals(VALID_NOMBRE, request.getNombre());
        assertEquals(VALID_ID_FUNCIONALIDAD, request.getIdFuncionalidad());
    }

    @Test
    void shouldThrowExceptionWhenNombreIsNull() {
        assertThrows(ParametroException.class, () -> {
            new CrearParametroDtoRequest(null, VALID_ID_FUNCIONALIDAD, VALID_ID_TIPO_PARAMETRO, VALID_ACTIVO);
        });
    }

    @Test
    void shouldThrowExceptionWhenNombreIsEmpty() {
        assertThrows(ParametroException.class, () -> {
            new CrearParametroDtoRequest("", VALID_ID_FUNCIONALIDAD, VALID_ID_TIPO_PARAMETRO, VALID_ACTIVO);
        });
    }

    @Test
    void shouldThrowExceptionWhenNombreIsBlank() {
        assertThrows(ParametroException.class, () -> {
            new CrearParametroDtoRequest("   ", VALID_ID_FUNCIONALIDAD, VALID_ID_TIPO_PARAMETRO, VALID_ACTIVO);
        });
    }

    @Test
    void shouldThrowExceptionWhenNombreIsTooShort() {
        assertThrows(ParametroException.class, () -> {
            new CrearParametroDtoRequest("AB", VALID_ID_FUNCIONALIDAD, VALID_ID_TIPO_PARAMETRO, VALID_ACTIVO);
        });
    }

    @Test
    void shouldThrowExceptionWhenNombreIsTooLong() {
        String longNombre = "A".repeat(51);
        assertThrows(ParametroException.class, () -> {
            new CrearParametroDtoRequest(longNombre, VALID_ID_FUNCIONALIDAD, VALID_ID_TIPO_PARAMETRO, VALID_ACTIVO);
        });
    }

    @Test
    void shouldAcceptNombreWithMinLength() {
        var request = new CrearParametroDtoRequest("ABC", VALID_ID_FUNCIONALIDAD, VALID_ID_TIPO_PARAMETRO, VALID_ACTIVO);

        assertEquals("ABC", request.getNombre());
    }

    @Test
    void shouldAcceptNombreWithMaxLength() {
        String maxNombre = "A".repeat(50);
        var request = new CrearParametroDtoRequest(maxNombre, VALID_ID_FUNCIONALIDAD, VALID_ID_TIPO_PARAMETRO, VALID_ACTIVO);

        assertEquals(maxNombre, request.getNombre());
    }

    // ==================== TESTS DE ID FUNCIONALIDAD ====================

    @Test
    void shouldThrowExceptionWhenIdFuncionalidadIsNull() {
        assertThrows(ParametroException.class, () -> {
            new CrearParametroDtoRequest(VALID_NOMBRE, null, VALID_ID_TIPO_PARAMETRO, VALID_ACTIVO);
        });
    }

    @Test
    void shouldThrowExceptionWhenIdFuncionalidadIsEmpty() {
        assertThrows(ParametroException.class, () -> {
            new CrearParametroDtoRequest(VALID_NOMBRE, "", VALID_ID_TIPO_PARAMETRO, VALID_ACTIVO);
        });
    }

    @Test
    void shouldThrowExceptionWhenIdFuncionalidadIsInvalid() {
        assertThrows(ParametroException.class, () -> {
            new CrearParametroDtoRequest(VALID_NOMBRE, "invalid-uuid", VALID_ID_TIPO_PARAMETRO, VALID_ACTIVO);
        });
    }

    @Test
    void shouldAcceptValidIdFuncionalidad() {
        var request = new CrearParametroDtoRequest(VALID_NOMBRE, VALID_ID_FUNCIONALIDAD, VALID_ID_TIPO_PARAMETRO, VALID_ACTIVO);

        assertEquals(VALID_ID_FUNCIONALIDAD, request.getIdFuncionalidad());
    }

    // ==================== TESTS DE ID TIPO PARAMETRO ====================

    @Test
    void shouldThrowExceptionWhenIdTipoParametroIsNull() {
        assertThrows(ParametroException.class, () -> {
            new CrearParametroDtoRequest(VALID_NOMBRE, VALID_ID_FUNCIONALIDAD, null, VALID_ACTIVO);
        });
    }

    @Test
    void shouldThrowExceptionWhenIdTipoParametroIsEmpty() {
        assertThrows(ParametroException.class, () -> {
            new CrearParametroDtoRequest(VALID_NOMBRE, VALID_ID_FUNCIONALIDAD, "", VALID_ACTIVO);
        });
    }

    @Test
    void shouldThrowExceptionWhenIdTipoParametroIsInvalid() {
        assertThrows(ParametroException.class, () -> {
            new CrearParametroDtoRequest(VALID_NOMBRE, VALID_ID_FUNCIONALIDAD, "not-a-uuid", VALID_ACTIVO);
        });
    }

    @Test
    void shouldAcceptValidIdTipoParametro() {
        var request = new CrearParametroDtoRequest(VALID_NOMBRE, VALID_ID_FUNCIONALIDAD, VALID_ID_TIPO_PARAMETRO, VALID_ACTIVO);

        assertEquals(VALID_ID_TIPO_PARAMETRO, request.getIdTipoParametro());
    }

    // ==================== TESTS DE ACTIVO ====================

    @Test
    void shouldAcceptTrueAsActivo() {
        var request = new CrearParametroDtoRequest(VALID_NOMBRE, VALID_ID_FUNCIONALIDAD, VALID_ID_TIPO_PARAMETRO, "true");

        assertEquals("true", request.getActivo());
    }

    @Test
    void shouldAcceptFalseAsActivo() {
        var request = new CrearParametroDtoRequest(VALID_NOMBRE, VALID_ID_FUNCIONALIDAD, VALID_ID_TIPO_PARAMETRO, "false");

        assertEquals("false", request.getActivo());
    }

    @Test
    void shouldAcceptTrueWithDifferentCase() {
        var request = new CrearParametroDtoRequest(VALID_NOMBRE, VALID_ID_FUNCIONALIDAD, VALID_ID_TIPO_PARAMETRO, "TRUE");

        assertEquals("true", request.getActivo());
    }

    @Test
    void shouldAcceptFalseWithDifferentCase() {
        var request = new CrearParametroDtoRequest(VALID_NOMBRE, VALID_ID_FUNCIONALIDAD, VALID_ID_TIPO_PARAMETRO, "FALSE");

        assertEquals("false", request.getActivo());
    }

    @Test
    void shouldDefaultToTrueWhenActivoIsNull() {
        var request = new CrearParametroDtoRequest(VALID_NOMBRE, VALID_ID_FUNCIONALIDAD, VALID_ID_TIPO_PARAMETRO, null);

        assertEquals("true", request.getActivo());
    }

    @ParameterizedTest
    @ValueSource(strings = {"1", "yes", "no", "invalid", "TRUE123", "FALSE123"})
    void shouldThrowExceptionWhenActivoIsInvalid(String activo) {
        assertThrows(ParametroException.class, () -> {
            new CrearParametroDtoRequest(VALID_NOMBRE, VALID_ID_FUNCIONALIDAD, VALID_ID_TIPO_PARAMETRO, activo);
        });
    }

    // ==================== TESTS DE SETTERS ====================

    @Test
    void shouldUpdateNombreThroughSetter() {
        var request = CrearParametroDtoRequest.create(VALID_NOMBRE, VALID_ID_FUNCIONALIDAD, VALID_ID_TIPO_PARAMETRO, VALID_ACTIVO);
        request.setNombre("NuevoNombre");

        assertEquals("NuevoNombre", request.getNombre());
    }

    @Test
    void shouldUpdateIdFuncionalidadThroughSetter() {
        var request = CrearParametroDtoRequest.create(VALID_NOMBRE, VALID_ID_FUNCIONALIDAD, VALID_ID_TIPO_PARAMETRO, VALID_ACTIVO);
        var newId = UUID.randomUUID().toString();
        request.setIdFuncionalidad(newId);

        assertEquals(newId, request.getIdFuncionalidad());
    }

    @Test
    void shouldUpdateIdTipoParametroThroughSetter() {
        var request = CrearParametroDtoRequest.create(VALID_NOMBRE, VALID_ID_FUNCIONALIDAD, VALID_ID_TIPO_PARAMETRO, VALID_ACTIVO);
        var newId = UUID.randomUUID().toString();
        request.setIdTipoParametro(newId);

        assertEquals(newId, request.getIdTipoParametro());
    }

    @Test
    void shouldUpdateActivoThroughSetter() {
        var request = CrearParametroDtoRequest.create(VALID_NOMBRE, VALID_ID_FUNCIONALIDAD, VALID_ID_TIPO_PARAMETRO, VALID_ACTIVO);
        request.setActivo("false");

        assertEquals("false", request.getActivo());
    }
}
