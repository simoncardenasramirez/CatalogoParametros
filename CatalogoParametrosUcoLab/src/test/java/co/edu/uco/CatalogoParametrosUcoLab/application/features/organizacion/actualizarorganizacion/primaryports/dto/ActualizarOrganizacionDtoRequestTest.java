package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

class ActualizarOrganizacionDtoRequestTest {

    @Test
    void debeLanzarValidationExceptionCuandoSeUsaElConstructorSinArgumentos() {
        assertThrows(ValidationException.class, () -> new ActualizarOrganizacionDtoRequest());
    }

    @Test
    void debeAplicarTrimAlNombreCuandoSeCreaConEspacios() {
        var dto = ActualizarOrganizacionDtoRequest.create("  organizacion  ");
        assertEquals("organizacion", dto.getNombre());
    }

    @Test
    void debeAceptarUnNombreConLongitudValida() {
        var dto = ActualizarOrganizacionDtoRequest.create("organizacion");
        assertEquals("organizacion", dto.getNombre());
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreEstaVacio() {
        assertThrows(ValidationException.class, () -> ActualizarOrganizacionDtoRequest.create(" "));
        assertThrows(ValidationException.class, () -> ActualizarOrganizacionDtoRequest.create(null));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreTieneLongitudInvalida() {
        assertThrows(ValidationException.class, () -> ActualizarOrganizacionDtoRequest.create("ab"));
        assertThrows(ValidationException.class, () -> ActualizarOrganizacionDtoRequest.create("a".repeat(51)));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElSetNombreRecibeUnValorInvalido() {
        var dto = ActualizarOrganizacionDtoRequest.create("organizacion");
        assertThrows(ValidationException.class, () -> dto.setNombre("  "));
    }
}