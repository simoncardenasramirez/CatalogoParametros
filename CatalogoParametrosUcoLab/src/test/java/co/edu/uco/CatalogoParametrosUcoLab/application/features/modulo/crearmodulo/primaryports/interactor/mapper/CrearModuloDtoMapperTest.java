package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.interactor.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.dto.CrearModuloDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.primaryports.dto.CrearModuloDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.usecase.domain.CrearModuloDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

class CrearModuloDtoMapperTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String FECHA_INICIO = "2026-01-01 00:00:00";
    private static final String FECHA_FINAL = "2026-12-31 23:59:59";

    @Test
    void debeConvertirRequestEnInputConDatosReales() {
        var idAplicacion = UUID.randomUUID().toString();
        var request = CrearModuloDtoRequest.create("modulo", idAplicacion, "true", FECHA_INICIO, FECHA_FINAL);

        CrearModuloDtoInput input = CrearModuloDtoMapper.INSTANCE.toDtoInput(request);

        assertEquals("modulo", input.getNombre());
        assertEquals(UUID.fromString(idAplicacion), input.getIdAplicacion());
        assertTrue(input.isActivo());
        assertEquals(LocalDateTime.parse(FECHA_INICIO, DATE_FORMATTER), input.getFechaInicio());
        assertEquals(LocalDateTime.parse(FECHA_FINAL, DATE_FORMATTER), input.getFechaFinal());
    }

    @Test
    void debeLanzarValidationExceptionCuandoElRequestEsNulo() {
        assertThrows(ValidationException.class, () -> CrearModuloDtoMapper.INSTANCE.toDtoInput(null));
    }

    @Test
    void debeConvertirInputEnDomainConIdGeneradoCuandoLosDatosSonValidos() {
        var idAplicacion = UUID.randomUUID();
        var input = CrearModuloDtoInput.create("modulo", idAplicacion, true,
                LocalDateTime.parse(FECHA_INICIO, DATE_FORMATTER),
                LocalDateTime.parse(FECHA_FINAL, DATE_FORMATTER));

        CrearModuloDomain domain = CrearModuloDtoMapper.INSTANCE.toDomain(input);

        assertNotNull(domain.getId());
        assertNotEquals(UUIDHelper.getDefault(), domain.getId());
        assertEquals("modulo", domain.getNombre());
        assertEquals(idAplicacion, domain.getIdAplicacion());
        assertTrue(domain.isActivo());
        assertEquals(LocalDateTime.parse(FECHA_INICIO, DATE_FORMATTER), domain.getFechaInicio());
        assertEquals(LocalDateTime.parse(FECHA_FINAL, DATE_FORMATTER), domain.getFechaFinal());
    }

    @Test
    void debeSerEquivalenteToDomainDeRequestYTdeInput() {
        var request = CrearModuloDtoRequest.create("modulo", UUID.randomUUID().toString(), "false",
                FECHA_INICIO, FECHA_FINAL);

        var viaRequest = CrearModuloDtoMapper.INSTANCE.toDomain(request);
        var viaInput = CrearModuloDtoMapper.INSTANCE.toDomain(CrearModuloDtoMapper.INSTANCE.toDtoInput(request));

        assertEquals(viaRequest.getNombre(), viaInput.getNombre());
        assertEquals(viaRequest.getIdAplicacion(), viaInput.getIdAplicacion());
        assertEquals(viaRequest.isActivo(), viaInput.isActivo());
        assertEquals(viaRequest.getFechaInicio(), viaInput.getFechaInicio());
        assertEquals(viaRequest.getFechaFinal(), viaInput.getFechaFinal());
    }
}