package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

class CrearParametroDtoRequestTest {

    private String idValido() {
        return UUID.randomUUID().toString();
    }

    @Test
    void debeLanzarValidationExceptionCuandoSeUsaElConstructorSinArgumentos() {
        assertThrows(ValidationException.class, CrearParametroDtoRequest::new);
    }

    @Test
    void debeAplicarTrimAlNombreCuandoSeCreaConEspacios() {
        var dto = CrearParametroDtoRequest.create("  parametro  ", idValido(), idValido(), "true");

        assertEquals("parametro", dto.getNombre());
    }

    @Test
    void debeAsignarTruePorDefectoCuandoElActivoEsNulo() {
        var dto = CrearParametroDtoRequest.create("parametro", idValido(), idValido(), null);

        assertEquals("true", dto.getActivo());
    }

    @Test
    void debeNormalizarElActivoAMinusculas() {
        var dto = CrearParametroDtoRequest.create("parametro", idValido(), idValido(), "  TRUE  ");

        assertEquals("true", dto.getActivo());
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreEstaVacio() {
        assertThrows(ValidationException.class,
                () -> CrearParametroDtoRequest.create("   ", idValido(), idValido(), "true"));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreEsNulo() {
        assertThrows(ValidationException.class,
                () -> CrearParametroDtoRequest.create(null, idValido(), idValido(), "true"));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreTieneLongitudInvalida() {
        assertThrows(ValidationException.class,
                () -> CrearParametroDtoRequest.create("ab", idValido(), idValido(), "true"));
        assertThrows(ValidationException.class,
                () -> CrearParametroDtoRequest.create("a".repeat(51), idValido(), idValido(), "true"));
    }

    @Test
    void debeAceptarUnNombreEnElLimiteDeLongitud() {
        var dto = CrearParametroDtoRequest.create("abc", idValido(), idValido(), "true");
        assertEquals("abc", dto.getNombre());

        var dtoLargo = CrearParametroDtoRequest.create("a".repeat(50), idValido(), idValido(), "true");
        assertEquals(50, dtoLargo.getNombre().length());
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdFuncionalidadNoEsUuid() {
        assertThrows(ValidationException.class,
                () -> CrearParametroDtoRequest.create("parametro", "no-es-uuid", idValido(), "true"));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElIdTipoParametroNoEsUuid() {
        assertThrows(ValidationException.class,
                () -> CrearParametroDtoRequest.create("parametro", idValido(), "no-es-uuid", "true"));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElActivoNoEsTrueNiFalse() {
        assertThrows(ValidationException.class,
                () -> CrearParametroDtoRequest.create("parametro", idValido(), idValido(), "si"));
    }
}