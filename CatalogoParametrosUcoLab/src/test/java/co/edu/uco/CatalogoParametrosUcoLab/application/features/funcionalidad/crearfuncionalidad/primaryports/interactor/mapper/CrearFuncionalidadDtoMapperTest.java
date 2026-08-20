package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.interactor.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.dto.CrearFuncionalidadDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.primaryports.dto.CrearFuncionalidadDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

class CrearFuncionalidadDtoMapperTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String FECHA_INICIO = "2024-01-01 00:00:00";
    private static final String FECHA_FINAL = "2024-12-31 23:59:59";

    private CrearFuncionalidadDtoRequest requestValido() {
        return CrearFuncionalidadDtoRequest.create("funcionalidad", UUID.randomUUID().toString(), "true",
                FECHA_INICIO, FECHA_FINAL);
    }

    @Test
    void debeConvertirRequestTodoStringEnInputConDatosReales() {
        var idModulo = UUID.randomUUID().toString();
        var request = CrearFuncionalidadDtoRequest.create("funcionalidad", idModulo, "true", FECHA_INICIO, FECHA_FINAL);

        CrearFuncionalidadDtoInput input = CrearFuncionalidadDtoMapper.INSTANCE.toDtoInput(request);

        assertEquals("funcionalidad", input.getNombre());
        assertEquals(UUID.fromString(idModulo), input.getIdModulo());
        assertTrue(input.isActivo());
        assertEquals(LocalDateTime.parse(FECHA_INICIO, DATE_FORMATTER), input.getFechaInicio());
        assertEquals(LocalDateTime.parse(FECHA_FINAL, DATE_FORMATTER), input.getFechaFinal());
    }

    @Test
    void debeConvertirRequestEnDomainConLosMismosDatos() {
        var request = requestValido();

        var domain = CrearFuncionalidadDtoMapper.INSTANCE.toDomain(request);

        assertEquals(request.getNombre(), domain.getNombre());
        assertEquals(UUID.fromString(request.getIdModulo()), domain.getIdModulo());
        assertTrue(domain.isActivo());
        assertNotNull(domain.getId());
    }

    @Test
    void debeLanzarValidationExceptionCuandoElRequestEsNulo() {
        assertThrows(ValidationException.class, () -> CrearFuncionalidadDtoMapper.INSTANCE.toDtoInput(null));
    }

    @Test
    void debeSerEquivalenteToDomainDeRequestYDeInput() {
        var request = CrearFuncionalidadDtoRequest.create("funcionalidad", UUID.randomUUID().toString(), "false",
                FECHA_INICIO, FECHA_FINAL);

        var viaRequest = CrearFuncionalidadDtoMapper.INSTANCE.toDomain(request);
        var viaInput = CrearFuncionalidadDtoMapper.INSTANCE
                .toDomain(CrearFuncionalidadDtoMapper.INSTANCE.toDtoInput(request));

        assertEquals(viaRequest.getNombre(), viaInput.getNombre());
        assertEquals(viaRequest.getIdModulo(), viaInput.getIdModulo());
        assertEquals(viaRequest.isActivo(), viaInput.isActivo());
        assertEquals(viaRequest.getFechaInicio(), viaInput.getFechaInicio());
        assertEquals(viaRequest.getFechaFinal(), viaInput.getFechaFinal());
    }
}