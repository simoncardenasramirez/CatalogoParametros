package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.primaryports.interactor.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.primaryports.dto.ActualizarAplicacionDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.primaryports.dto.ActualizarAplicacionDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

class ActualizarAplicacionDtoMapperTest {

    private static final String FECHA_INICIO = "2024-01-01 00:00:00";
    private static final String FECHA_FINAL = "2024-12-31 23:59:59";

    private ActualizarAplicacionDtoRequest requestValido() {
        return ActualizarAplicacionDtoRequest.create("aplicacion", UUID.randomUUID().toString(), "true",
                FECHA_INICIO, FECHA_FINAL);
    }

    @Test
    void debeConvertirRequestTodoStringEnInputConDatosReales() {
        var idOrganizacion = UUID.randomUUID().toString();
        var request = ActualizarAplicacionDtoRequest.create("aplicacion", idOrganizacion, "true", FECHA_INICIO, FECHA_FINAL);

        ActualizarAplicacionDtoInput input = ActualizarAplicacionDtoMapper.INSTANCE.toDtoInput(request);

        assertEquals("aplicacion", input.getNombre());
        assertEquals(UUID.fromString(idOrganizacion), input.getIdOrganizacion());
        assertTrue(input.isActiva());
        assertEquals(LocalDateTime.parse(FECHA_INICIO,
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), input.getFechaInicio());
        assertEquals(LocalDateTime.parse(FECHA_FINAL,
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), input.getFechaFinal());
    }

    @Test
    void debeConvertirRequestEnDomainConElMismoId() {
        var id = UUID.randomUUID();
        var request = requestValido();

        var domain = ActualizarAplicacionDtoMapper.INSTANCE.toDomain(id, request);

        assertEquals(id, domain.getId());
        assertEquals(request.getNombre(), domain.getNombre());
        assertEquals(UUID.fromString(request.getIdOrganizacion()), domain.getIdOrganizacion());
        assertTrue(domain.isActiva());
        assertNotNull(domain.getFechaInicio());
    }

    @Test
    void debeLanzarValidationExceptionCuandoElRequestEsNulo() {
        assertThrows(ValidationException.class, () -> ActualizarAplicacionDtoMapper.INSTANCE.toDtoInput(null));
    }

    @Test
    void debeSerEquivalenteToDomainDeRequestYDeInput() {
        var id = UUID.randomUUID();
        var request = ActualizarAplicacionDtoRequest.create("aplicacion", UUID.randomUUID().toString(), "false",
                FECHA_INICIO, FECHA_FINAL);

        var viaRequest = ActualizarAplicacionDtoMapper.INSTANCE.toDomain(id, request);
        var viaInput = ActualizarAplicacionDtoMapper.INSTANCE.toDomain(id,
                ActualizarAplicacionDtoMapper.INSTANCE.toDtoInput(request));

        assertEquals(viaRequest.getId(), viaInput.getId());
        assertEquals(viaRequest.getNombre(), viaInput.getNombre());
        assertEquals(viaRequest.getIdOrganizacion(), viaInput.getIdOrganizacion());
        assertEquals(viaRequest.isActiva(), viaInput.isActiva());
        assertEquals(viaRequest.getFechaInicio(), viaInput.getFechaInicio());
        assertEquals(viaRequest.getFechaFinal(), viaInput.getFechaFinal());
    }
}