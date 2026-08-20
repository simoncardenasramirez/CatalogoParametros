package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.primaryports.interactor.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.primaryports.dto.ActualizarModuloDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.primaryports.dto.ActualizarModuloDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.usecase.domain.ActualizarModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

class ActualizarModuloDtoMapperTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String FECHA_INICIO = "2026-01-01 00:00:00";
    private static final String FECHA_FINAL = "2026-12-31 23:59:59";

    @Test
    void debeConvertirRequestEnInputConDatosReales() {
        var idAplicacion = UUID.randomUUID().toString();
        var request = ActualizarModuloDtoRequest.create("modulo", idAplicacion, "true", FECHA_INICIO, FECHA_FINAL);

        ActualizarModuloDtoInput input = ActualizarModuloDtoMapper.INSTANCE.toDtoInput(request);

        assertEquals("modulo", input.getNombre());
        assertEquals(UUID.fromString(idAplicacion), input.getIdAplicacion());
        assertTrue(input.isActivo());
        assertEquals(LocalDateTime.parse(FECHA_INICIO, DATE_FORMATTER), input.getFechaInicio());
        assertEquals(LocalDateTime.parse(FECHA_FINAL, DATE_FORMATTER), input.getFechaFinal());
    }

    @Test
    void debeLanzarValidationExceptionCuandoElRequestEsNulo() {
        assertThrows(ValidationException.class, () -> ActualizarModuloDtoMapper.INSTANCE.toDtoInput(null));
    }

    @Test
    void debeConvertirInputEnDomainConservandoElIdCuandoLosDatosSonValidos() {
        var id = UUID.randomUUID();
        var idAplicacion = UUID.randomUUID();
        var input = ActualizarModuloDtoInput.create("modulo", idAplicacion, true,
                LocalDateTime.parse(FECHA_INICIO, DATE_FORMATTER),
                LocalDateTime.parse(FECHA_FINAL, DATE_FORMATTER));

        ActualizarModuloDomain domain = ActualizarModuloDtoMapper.INSTANCE.toDomain(id, input);

        assertEquals(id, domain.getId());
        assertEquals("modulo", domain.getNombre());
        assertEquals(idAplicacion, domain.getIdAplicacion());
        assertTrue(domain.isActivo());
        assertEquals(LocalDateTime.parse(FECHA_INICIO, DATE_FORMATTER), domain.getFechaInicio());
        assertEquals(LocalDateTime.parse(FECHA_FINAL, DATE_FORMATTER), domain.getFechaFinal());
    }

    @Test
    void debeSerEquivalenteToDomainDeRequestYTdeInput() {
        var id = UUID.randomUUID();
        var request = ActualizarModuloDtoRequest.create("modulo", UUID.randomUUID().toString(), "false",
                FECHA_INICIO, FECHA_FINAL);

        var viaRequest = ActualizarModuloDtoMapper.INSTANCE.toDomain(id, request);
        var viaInput = ActualizarModuloDtoMapper.INSTANCE.toDomain(id,
                ActualizarModuloDtoMapper.INSTANCE.toDtoInput(request));

        assertEquals(viaRequest.getId(), viaInput.getId());
        assertEquals(viaRequest.getNombre(), viaInput.getNombre());
        assertEquals(viaRequest.getIdAplicacion(), viaInput.getIdAplicacion());
        assertEquals(viaRequest.isActivo(), viaInput.isActivo());
        assertEquals(viaRequest.getFechaInicio(), viaInput.getFechaInicio());
        assertEquals(viaRequest.getFechaFinal(), viaInput.getFechaFinal());
    }
}