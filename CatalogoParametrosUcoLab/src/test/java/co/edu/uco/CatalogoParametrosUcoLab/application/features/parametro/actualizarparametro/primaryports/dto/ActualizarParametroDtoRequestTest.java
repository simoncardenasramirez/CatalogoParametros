package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

class ActualizarParametroDtoRequestTest {

    private String idValido() {
        return UUID.randomUUID().toString();
    }

    @Test
    void debeLanzarValidationExceptionCuandoSeUsaElConstructorSinArgumentos() {
        assertThrows(ValidationException.class, ActualizarParametroDtoRequest::new);
    }

    @Test
    void debeAplicarTrimAlNombreCuandoSeCreaConEspacios() {
        var dto = ActualizarParametroDtoRequest.create("  parametro  ", idValido(), idValido(), "true");

        assertEquals("parametro", dto.getNombre());
    }

    @Test
    void debeAsignarTruePorDefectoCuandoElActivoEsNulo() {
        var dto = ActualizarParametroDtoRequest.create("parametro", idValido(), idValido(), null);

        assertEquals("true", dto.getActivo());
    }

    @Test
    void debeNormalizarElActivoAMinusculas() {
        var dto = ActualizarParametroDtoRequest.create("parametro", idValido(), idValido(), "  FALSE  ");

        assertEquals("false", dto.getActivo());
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreEstaVacio() {
        assertThrows(ValidationException.class,
                () -> ActualizarParametroDtoRequest.create("   ", idValido(), idValido(), "true"));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreEsNulo() {
        assertThrows(ValidationException.class,
                () -> ActualizarParametroDtoRequest.create(null, idValido(), idValido(), "true"));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreTieneLongitudInvalida() {
        assertThrows(ValidationException.class,
                () -> ActualizarParametroDtoRequest.create("ab", idValido(), idValido(), "true"));
        assertThrows(ValidationException.class,
                () -> ActualizarParametroDtoRequest.create("a".repeat(51), idValido(), idValido(), "true"));
    }

    @Test
    void debeAceptarUnNombreEnElLimiteDeLongitud() {
        var dto = ActualizarParametroDtoRequest.create("abc", idValido(), idValido(), "true");
        assertEquals("abc", dto.getNombre());

        var dtoLargo = ActualizarParametroDtoRequest.create("a".repeat(50), idValido(), idValido(), "true");
        assertEquals(50, dtoLargo.getNombre().length());
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdFuncionalidadNoEsUuid() {
        assertThrows(ValidationException.class,
                () -> ActualizarParametroDtoRequest.create("parametro", "no-es-uuid", idValido(), "true"));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdTipoParametroNoEsUuid() {
        assertThrows(ValidationException.class,
                () -> ActualizarParametroDtoRequest.create("parametro", idValido(), "no-es-uuid", "true"));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElActivoNoEsTrueNiFalse() {
        assertThrows(ValidationException.class,
                () -> ActualizarParametroDtoRequest.create("parametro", idValido(), idValido(), "si"));
    }
}