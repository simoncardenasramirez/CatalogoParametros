package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.primaryports.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

class ActualizarModuloDtoRequestTest {

    private static final String FECHA_VALIDA = "2026-01-01 00:00:00";

    @Test
    void debeCrearConLosValoresCuandoLosDatosSonValidos() {
        var idAplicacion = UUID.randomUUID().toString();
        var dto = ActualizarModuloDtoRequest.create("modulo", idAplicacion, "true",
                FECHA_VALIDA, "2026-12-31 23:59:59");

        assertEquals("modulo", dto.getNombre());
        assertEquals(idAplicacion, dto.getIdAplicacion());
        assertEquals("true", dto.getActivo());
        assertEquals(FECHA_VALIDA, dto.getFechaInicio());
        assertEquals("2026-12-31 23:59:59", dto.getFechaFinal());
    }

    @Test
    void debeRecortarElNombreCuandoTieneEspaciosAlInicioYAlFinal() {
        var dto = ActualizarModuloDtoRequest.create("  modulo  ", UUID.randomUUID().toString(), "true",
                FECHA_VALIDA, "");

        assertEquals("modulo", dto.getNombre());
    }

    @Test
    void debeLanzarValidationExceptionCuandoSeUsaElConstructorSinArgumentos() {
        assertThrows(ValidationException.class, () -> new ActualizarModuloDtoRequest());
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreEstaVacio() {
        assertThrows(ValidationException.class,
                () -> ActualizarModuloDtoRequest.create("  ", UUID.randomUUID().toString(), "true", FECHA_VALIDA, ""));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreEsNulo() {
        assertThrows(ValidationException.class,
                () -> ActualizarModuloDtoRequest.create(null, UUID.randomUUID().toString(), "true", FECHA_VALIDA, ""));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreTieneLongitudInvalida() {
        assertThrows(ValidationException.class,
                () -> ActualizarModuloDtoRequest.create("ab", UUID.randomUUID().toString(), "true", FECHA_VALIDA, ""));
        assertThrows(ValidationException.class,
                () -> ActualizarModuloDtoRequest.create("a".repeat(51), UUID.randomUUID().toString(), "true", FECHA_VALIDA, ""));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdAplicacionEstaVacio() {
        assertThrows(ValidationException.class,
                () -> ActualizarModuloDtoRequest.create("modulo", " ", "true", FECHA_VALIDA, ""));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdAplicacionNoEsUuid() {
        assertThrows(ValidationException.class,
                () -> ActualizarModuloDtoRequest.create("modulo", "no-es-uuid", "true", FECHA_VALIDA, ""));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElActivoNoEsTrueNiFalse() {
        assertThrows(ValidationException.class,
                () -> ActualizarModuloDtoRequest.create("modulo", UUID.randomUUID().toString(), "si", FECHA_VALIDA, ""));
    }

    @Test
    void debeNormalizarElActivoEnMinusculasCuandoVieneEnMayusculas() {
        var dto = ActualizarModuloDtoRequest.create("modulo", UUID.randomUUID().toString(), "TRUE", FECHA_VALIDA, "");

        assertEquals("true", dto.getActivo());
    }

    @Test
    void debeUsarTrueComoActivoPorDefectoCuandoElActivoEsNulo() {
        var dto = ActualizarModuloDtoRequest.create("modulo", UUID.randomUUID().toString(), null, FECHA_VALIDA, "");

        assertEquals("true", dto.getActivo());
    }

    @Test
    void debeLanzarValidationExceptionCuandoLaFechaInicioNoTieneFormatoValido() {
        assertThrows(ValidationException.class,
                () -> ActualizarModuloDtoRequest.create("modulo", UUID.randomUUID().toString(), "true",
                        "01-01-2026 00:00:00", ""));
    }

    @Test
    void debeLanzarValidationExceptionCuandoLaFechaFinalNoTieneFormatoValido() {
        assertThrows(ValidationException.class,
                () -> ActualizarModuloDtoRequest.create("modulo", UUID.randomUUID().toString(), "true",
                        FECHA_VALIDA, "31-12-2026 23:59:59"));
    }

    @Test
    void debeAceptarFechasVaciasCuandoNoSeEnvian() {
        var dto = ActualizarModuloDtoRequest.create("modulo", UUID.randomUUID().toString(), "true", "", "");

        assertEquals("", dto.getFechaInicio());
        assertEquals("", dto.getFechaFinal());
    }
}