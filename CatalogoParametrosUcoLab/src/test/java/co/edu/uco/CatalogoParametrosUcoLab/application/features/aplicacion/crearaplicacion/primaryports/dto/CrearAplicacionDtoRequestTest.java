package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.primaryports.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

class CrearAplicacionDtoRequestTest {

    private static final String FECHA_VALIDA = "2024-01-01 00:00:00";
    private static final String UUID_VALIDO = UUID.randomUUID().toString();

    @Test
    void debeLanzarValidationExceptionCuandoSeUsaElConstructorSinArgumentos() {
        assertThrows(ValidationException.class, CrearAplicacionDtoRequest::new);
    }

    @Test
    void debeCrearConDatosRealesCuandoLosValoresSonValidos() {
        var dto = CrearAplicacionDtoRequest.create("aplicacion", UUID_VALIDO, "true", FECHA_VALIDA, "2024-12-31 23:59:59");

        assertEquals("aplicacion", dto.getNombre());
        assertEquals(UUID_VALIDO, dto.getIdOrganizacion());
        assertEquals("true", dto.getActiva());
        assertEquals(FECHA_VALIDA, dto.getFechaInicio());
        assertEquals("2024-12-31 23:59:59", dto.getFechaFinal());
    }

    @Test
    void debeAplicarTrimAlNombreYNormalizarElEstadoActivo() {
        var dto = CrearAplicacionDtoRequest.create("  aplicacion  ", UUID_VALIDO, " TRUE ", FECHA_VALIDA, FECHA_VALIDA);

        assertEquals("aplicacion", dto.getNombre());
        assertEquals("true", dto.getActiva());
    }

    @Test
    void debeUsarTruePorDefectoCuandoElEstadoActivoEsNulo() {
        var dto = CrearAplicacionDtoRequest.create("aplicacion", UUID_VALIDO, null, FECHA_VALIDA, FECHA_VALIDA);

        assertEquals("true", dto.getActiva());
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreEstaVacio() {
        assertThrows(ValidationException.class,
                () -> CrearAplicacionDtoRequest.create(" ", UUID_VALIDO, "true", FECHA_VALIDA, FECHA_VALIDA));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreTieneLongitudInvalida() {
        assertThrows(ValidationException.class,
                () -> CrearAplicacionDtoRequest.create("ab", UUID_VALIDO, "true", FECHA_VALIDA, FECHA_VALIDA));
        assertThrows(ValidationException.class,
                () -> CrearAplicacionDtoRequest.create("a".repeat(51), UUID_VALIDO, "true", FECHA_VALIDA, FECHA_VALIDA));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdOrganizacionEstaVacio() {
        assertThrows(ValidationException.class,
                () -> CrearAplicacionDtoRequest.create("aplicacion", " ", "true", FECHA_VALIDA, FECHA_VALIDA));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdOrganizacionNoEsUuid() {
        assertThrows(ValidationException.class,
                () -> CrearAplicacionDtoRequest.create("aplicacion", "no-es-uuid", "true", FECHA_VALIDA, FECHA_VALIDA));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElActivoNoEsTrueNiFalse() {
        assertThrows(ValidationException.class,
                () -> CrearAplicacionDtoRequest.create("aplicacion", UUID_VALIDO, "si", FECHA_VALIDA, FECHA_VALIDA));
    }

    @Test
    void debeLanzarValidationExceptionCuandoLaFechaInicioNoTieneFormatoValido() {
        assertThrows(ValidationException.class,
                () -> CrearAplicacionDtoRequest.create("aplicacion", UUID_VALIDO, "true", "01-01-2024", FECHA_VALIDA));
    }

    @Test
    void debeLanzarValidationExceptionCuandoLaFechaFinalNoTieneFormatoValido() {
        assertThrows(ValidationException.class,
                () -> CrearAplicacionDtoRequest.create("aplicacion", UUID_VALIDO, "true", FECHA_VALIDA, "31-12-2024"));
    }
}