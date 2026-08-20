package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

class CrearOrganizacionDtoRequestTest {

    @Test
    void debeLanzarValidationExceptionCuandoSeUsaElConstructorSinArgumentos() {
        assertThrows(ValidationException.class, () -> new CrearOrganizacionDtoRequest());
    }

    @Test
    void debeAplicarTrimAlNombreCuandoSeCreaConEspacios() {
        var dto = CrearOrganizacionDtoRequest.create("  organizacion  ");
        assertEquals("organizacion", dto.getNombre());
    }

    @Test
    void debeAceptarUnNombreConLongitudValida() {
        var dto = CrearOrganizacionDtoRequest.create("organizacion");
        assertEquals("organizacion", dto.getNombre());
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreEstaVacio() {
        assertThrows(ValidationException.class, () -> CrearOrganizacionDtoRequest.create(" "));
        assertThrows(ValidationException.class, () -> CrearOrganizacionDtoRequest.create(null));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElNombreTieneLongitudInvalida() {
        assertThrows(ValidationException.class, () -> CrearOrganizacionDtoRequest.create("ab"));
        assertThrows(ValidationException.class, () -> CrearOrganizacionDtoRequest.create("a".repeat(51)));
    }

    @Test
    void debeLanzarValidationExceptionCuandoElSetNombreRecibeUnValorInvalido() {
        var dto = CrearOrganizacionDtoRequest.create("organizacion");
        assertThrows(ValidationException.class, () -> dto.setNombre("  "));
    }
}