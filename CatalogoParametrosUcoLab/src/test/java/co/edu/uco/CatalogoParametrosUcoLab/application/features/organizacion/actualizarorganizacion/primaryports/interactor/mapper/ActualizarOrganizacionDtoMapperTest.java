package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.interactor.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto.ActualizarOrganizacionDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.primaryports.dto.ActualizarOrganizacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

class ActualizarOrganizacionDtoMapperTest {

    @Test
    void debeRechazarFechaFinalAnteriorALaInicial() {
        var request = ActualizarOrganizacionDtoRequest.create("organizacion",
                "2026-12-31 23:59:59", "2026-01-01 00:00:00");

        assertThrows(ValidationException.class, () -> ActualizarOrganizacionDtoMapper.INSTANCE.toDtoInput(request));
    }

    @Test
    void debeConvertirRequestEnInputConElNombre() {
        var request = ActualizarOrganizacionDtoRequest.create("organizacion");

        var input = ActualizarOrganizacionDtoMapper.INSTANCE.toDtoInput(request);

        assertEquals("organizacion", input.getNombre());
    }

@Test
    void debeLanzarValidationExceptionCuandoElRequestEsNulo() {
        assertThrows(ValidationException.class,
                () -> ActualizarOrganizacionDtoMapper.INSTANCE.toDtoInput(null));
        assertThrows(ValidationException.class,
                () -> ActualizarOrganizacionDtoMapper.INSTANCE.toDomain(UUID.randomUUID(),
                        (ActualizarOrganizacionDtoRequest) null));
    }

    @Test
    void debeConvertirIdYInputEnDomain() {
        var id = UUID.randomUUID();
        var input = ActualizarOrganizacionDtoInput.create("organizacion");

        var domain = ActualizarOrganizacionDtoMapper.INSTANCE.toDomain(id, input);

        assertEquals(id, domain.getId());
        assertEquals("organizacion", domain.getNombre());
    }

    @Test
    void debeSerEquivalenteToDomainDeRequestYDeInput() {
        var id = UUID.randomUUID();
        var request = ActualizarOrganizacionDtoRequest.create("organizacion");
        var viaRequest = ActualizarOrganizacionDtoMapper.INSTANCE.toDomain(id, request);
        var viaInput = ActualizarOrganizacionDtoMapper.INSTANCE.toDomain(id,
                ActualizarOrganizacionDtoMapper.INSTANCE.toDtoInput(request));
        assertEquals(viaRequest.getId(), viaInput.getId());
        assertEquals(viaRequest.getNombre(), viaInput.getNombre());
    }
}
