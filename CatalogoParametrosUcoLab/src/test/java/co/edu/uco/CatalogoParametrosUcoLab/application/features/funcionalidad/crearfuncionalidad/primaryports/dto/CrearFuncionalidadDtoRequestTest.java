package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

class CrearFuncionalidadDtoRequestTest {

    private static final String FECHA_VALIDA = "2024-01-01 00:00:00";
    private static final String UUID_VALIDO = UUID.randomUUID().toString();

    @Test
    void debeLanzarValidationExceptionCuandoSeUsaElConstructorSinArgumentos() {
        assertThrows(ValidationException.class, CrearFuncionalidadDtoRequest::new);
    }

    @Test
    void debeCrearConDatosRealesCuandoLosValoresSonValidos() {
        var dto = CrearFuncionalidadDtoRequest.create("funcionalidad", UUID_VALIDO, "true", FECHA_VALIDA,
                "2024-12-31 23:59:59");

        assertEquals("funcionalidad", dto.getNombre());
        assertEquals(UUID_VALIDO, dto.getIdModulo());
        assertEquals("true", dto.getActivo());
        assertEquals(FECHA_VALIDA, dto.getFechaInicio());
        assertEquals("2024-12-31 23:59:59", dto.getFechaFinal());
    }

    @Test
    void debeAplicarTrimAlNombreYNormalizarElEstadoActivo() {
        var dto = CrearFuncionalidadDtoRequest.create("  funcionalidad  ", UUID_VALIDO, " TRUE ", FECHA_VALIDA,
                FECHA_VALIDA);

        assertEquals("funcionalidad", dto.getNombre());
        assertEquals("true", dto.getActivo());
    }

    @Test
    void debeUsarTruePorDefectoCuandoElEstadoActivoEsNulo() {
        var dto = CrearFuncionalidadDtoRequest.create("funcionalidad", UUID_VALIDO, null, FECHA_VALIDA, FECHA_VALIDA);

        assertEquals("true", dto.getActivo());
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreEstaVacio() {
        assertThrows(ValidationException.class,
                () -> CrearFuncionalidadDtoRequest.create(" ", UUID_VALIDO, "true", FECHA_VALIDA, FECHA_VALIDA));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreTieneLongitudInvalida() {
        assertThrows(ValidationException.class,
                () -> CrearFuncionalidadDtoRequest.create("ab", UUID_VALIDO, "true", FECHA_VALIDA, FECHA_VALIDA));
        assertThrows(ValidationException.class,
                () -> CrearFuncionalidadDtoRequest.create("a".repeat(51), UUID_VALIDO, "true", FECHA_VALIDA,
                        FECHA_VALIDA));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdModuloEstaVacio() {
        assertThrows(ValidationException.class,
                () -> CrearFuncionalidadDtoRequest.create("funcionalidad", " ", "true", FECHA_VALIDA, FECHA_VALIDA));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdModuloNoEsUuid() {
        assertThrows(ValidationException.class,
                () -> CrearFuncionalidadDtoRequest.create("funcionalidad", "no-es-uuid", "true", FECHA_VALIDA,
                        FECHA_VALIDA));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElActivoNoEsTrueNiFalse() {
        assertThrows(ValidationException.class,
                () -> CrearFuncionalidadDtoRequest.create("funcionalidad", UUID_VALIDO, "si", FECHA_VALIDA,
                        FECHA_VALIDA));
    }

    @Test
    void debeLanzarValidationExceptionCuandoLaFechaInicioNoTieneFormatoValido() {
        assertThrows(ValidationException.class,
                () -> CrearFuncionalidadDtoRequest.create("funcionalidad", UUID_VALIDO, "true", "01-01-2024",
                        FECHA_VALIDA));
    }

    @Test
    void debeLanzarValidationExceptionCuandoLaFechaFinalNoTieneFormatoValido() {
        assertThrows(ValidationException.class,
                () -> CrearFuncionalidadDtoRequest.create("funcionalidad", UUID_VALIDO, "true", FECHA_VALIDA,
                        "31-12-2024"));
    }
}