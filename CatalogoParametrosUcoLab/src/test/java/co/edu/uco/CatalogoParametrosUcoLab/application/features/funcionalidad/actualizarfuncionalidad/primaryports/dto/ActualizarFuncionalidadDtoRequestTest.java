package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.primaryports.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

class ActualizarFuncionalidadDtoRequestTest {

    private static final String FECHA_VALIDA = "2024-01-01 00:00:00";
    private static final String UUID_VALIDO = UUID.randomUUID().toString();

    @Test
    void debeLanzarValidationExceptionCuandoSeUsaElConstructorSinArgumentos() {
        assertThrows(ValidationException.class, ActualizarFuncionalidadDtoRequest::new);
    }

    @Test
    void debeCrearConDatosRealesCuandoLosValoresSonValidos() {
        var dto = ActualizarFuncionalidadDtoRequest.create("funcionalidad", UUID_VALIDO, "true", FECHA_VALIDA,
                "2024-12-31 23:59:59");

        assertEquals("funcionalidad", dto.getNombre());
        assertEquals(UUID_VALIDO, dto.getIdModulo());
        assertEquals("true", dto.getActivo());
        assertEquals(FECHA_VALIDA, dto.getFechaInicio());
        assertEquals("2024-12-31 23:59:59", dto.getFechaFinal());
    }

    @Test
    void debeAplicarTrimAlNombreYNormalizarElEstadoActivo() {
        var dto = ActualizarFuncionalidadDtoRequest.create("  funcionalidad  ", UUID_VALIDO, " FALSE ", FECHA_VALIDA,
                FECHA_VALIDA);

        assertEquals("funcionalidad", dto.getNombre());
        assertEquals("false", dto.getActivo());
    }

    @Test
    void debeUsarTruePorDefectoCuandoElEstadoActivoEsNulo() {
        var dto = ActualizarFuncionalidadDtoRequest.create("funcionalidad", UUID_VALIDO, null, FECHA_VALIDA,
                FECHA_VALIDA);

        assertEquals("true", dto.getActivo());
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreEstaVacio() {
        assertThrows(ValidationException.class,
                () -> ActualizarFuncionalidadDtoRequest.create(" ", UUID_VALIDO, "true", FECHA_VALIDA, FECHA_VALIDA));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreTieneLongitudInvalida() {
        assertThrows(ValidationException.class,
                () -> ActualizarFuncionalidadDtoRequest.create("ab", UUID_VALIDO, "true", FECHA_VALIDA, FECHA_VALIDA));
        assertThrows(ValidationException.class,
                () -> ActualizarFuncionalidadDtoRequest.create("a".repeat(51), UUID_VALIDO, "true", FECHA_VALIDA,
                        FECHA_VALIDA));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdModuloEstaVacio() {
        assertThrows(ValidationException.class,
                () -> ActualizarFuncionalidadDtoRequest.create("funcionalidad", " ", "true", FECHA_VALIDA,
                        FECHA_VALIDA));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdModuloNoEsUuid() {
        assertThrows(ValidationException.class,
                () -> ActualizarFuncionalidadDtoRequest.create("funcionalidad", "no-es-uuid", "true", FECHA_VALIDA,
                        FECHA_VALIDA));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElActivoNoEsTrueNiFalse() {
        assertThrows(ValidationException.class,
                () -> ActualizarFuncionalidadDtoRequest.create("funcionalidad", UUID_VALIDO, "si", FECHA_VALIDA,
                        FECHA_VALIDA));
    }

    @Test
    void debeLanzarValidationExceptionCuandoLaFechaInicioNoTieneFormatoValido() {
        assertThrows(ValidationException.class,
                () -> ActualizarFuncionalidadDtoRequest.create("funcionalidad", UUID_VALIDO, "true", "01-01-2024",
                        FECHA_VALIDA));
    }

    @Test
    void debeLanzarValidationExceptionCuandoLaFechaFinalNoTieneFormatoValido() {
        assertThrows(ValidationException.class,
                () -> ActualizarFuncionalidadDtoRequest.create("funcionalidad", UUID_VALIDO, "true", FECHA_VALIDA,
                        "31-12-2024"));
    }
}