package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.interactor.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.dto.CrearOrganizacionDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.primaryports.dto.CrearOrganizacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

class CrearOrganizacionDtoMapperTest {

    @Test
    void debeConvertirRequestEnInputConElNombreNormalizado() {
        var request = CrearOrganizacionDtoRequest.create("  organizacion  ");

        CrearOrganizacionDtoInput input = CrearOrganizacionDtoMapper.INSTANCE.toDtoInput(request);

        assertEquals("organizacion", input.getNombre());
    }

@Test
    void debeLanzarValidationExceptionCuandoElRequestEsNulo() {
        assertThrows(ValidationException.class,
                () -> CrearOrganizacionDtoMapper.INSTANCE.toDtoInput(null));
        assertThrows(ValidationException.class,
                () -> CrearOrganizacionDtoMapper.INSTANCE.toDomain((CrearOrganizacionDtoRequest) null));
    }

    @Test
    void debeConvertirInputEnDomainConIdGenerado() {
        var input = CrearOrganizacionDtoInput.create("organizacion");

        var domain = CrearOrganizacionDtoMapper.INSTANCE.toDomain(input);

        assertEquals("organizacion", domain.getNombre());
        assertNotEquals(UUIDHelper.getDefault(), domain.getId());
    }

    @Test
    void debeSerEquivalenteToDomainDeRequestYDeInput() {
        var request = CrearOrganizacionDtoRequest.create("organizacion");
        var viaRequest = CrearOrganizacionDtoMapper.INSTANCE.toDomain(request);
        var viaInput = CrearOrganizacionDtoMapper.INSTANCE.toDomain(
                CrearOrganizacionDtoMapper.INSTANCE.toDtoInput(request));
        assertEquals(viaRequest.getNombre(), viaInput.getNombre());
    }
}