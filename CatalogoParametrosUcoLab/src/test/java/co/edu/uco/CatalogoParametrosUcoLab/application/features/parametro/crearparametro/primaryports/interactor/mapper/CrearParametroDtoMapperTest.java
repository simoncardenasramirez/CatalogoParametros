package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.mapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto.CrearParametroDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto.CrearParametroDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.exception.ParametroException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CrearParametroDtoMapperTest {

    private static final String NOMBRE = "ParametroTest";
    private static final UUID ID_FUNCIONALIDAD = UUID.randomUUID();
    private static final UUID ID_TIPO_PARAMETRO = UUID.randomUUID();
    private static final boolean ACTIVO = true;

    // ==================== TESTS DE toDtoInput ====================

    @Test
    void shouldConvertDtoRequestToDtoInput() {
        var dtoRequest = CrearParametroDtoRequest.create(NOMBRE, ID_FUNCIONALIDAD.toString(), ID_TIPO_PARAMETRO.toString(), "true");
        var dtoInput = CrearParametroDtoMapper.INSTANCE.toDtoInput(dtoRequest);

        assertEquals(NOMBRE, dtoInput.getNombre());
        assertEquals(ID_FUNCIONALIDAD, dtoInput.getIdFuncionalidad());
        assertEquals(ID_TIPO_PARAMETRO, dtoInput.getIdTipoParametro());
        assertTrue(dtoInput.isActivo());
    }

    @Test
    void shouldConvertDtoRequestToDtoInputWithFalseActivo() {
        var dtoRequest = CrearParametroDtoRequest.create(NOMBRE, ID_FUNCIONALIDAD.toString(), ID_TIPO_PARAMETRO.toString(), "false");
        var dtoInput = CrearParametroDtoMapper.INSTANCE.toDtoInput(dtoRequest);

        assertFalse(dtoInput.isActivo());
    }

    @Test
    void shouldHandleNullDtoRequestInToDtoInput() {
        // Cuando se pasa null, el mapper crea un nuevo CrearParametroDtoRequest con valores por defecto
        // pero el constructor por defecto lanza excepcion porque nombre vacio no es valido
        assertThrows(ParametroException.class, () -> CrearParametroDtoMapper.INSTANCE.toDtoInput(null));
    }

    // ==================== TESTS DE toDomain (desde DtoInput) ====================

    @Test
    void shouldConvertDtoInputToDomain() {
        var dtoInput = CrearParametroDtoInput.create(NOMBRE, ID_FUNCIONALIDAD, ID_TIPO_PARAMETRO, ACTIVO);
        var domain = CrearParametroDtoMapper.INSTANCE.toDomain(dtoInput);

        assertNotNull(domain);
        assertEquals(NOMBRE, domain.getNombre());
        assertEquals(ID_FUNCIONALIDAD, domain.getIdFuncionalidad());
        assertEquals(ID_TIPO_PARAMETRO, domain.getIdTipoParametro());
        assertTrue(domain.isActivo());
    }

    @Test
    void shouldConvertDtoInputToDomainWithDefaultId() {
        var dtoInput = CrearParametroDtoInput.create(NOMBRE, ID_FUNCIONALIDAD, ID_TIPO_PARAMETRO, ACTIVO);
        var domain = CrearParametroDtoMapper.INSTANCE.toDomain(dtoInput);

        assertNotNull(domain.getId());
    }

    // ==================== TESTS DE toDomain (desde DtoRequest) ====================

    @Test
    void shouldConvertDtoRequestToDomain() {
        var dtoRequest = CrearParametroDtoRequest.create(NOMBRE, ID_FUNCIONALIDAD.toString(), ID_TIPO_PARAMETRO.toString(), "true");
        var domain = CrearParametroDtoMapper.INSTANCE.toDomain(dtoRequest);

        assertNotNull(domain);
        assertEquals(NOMBRE, domain.getNombre());
        assertEquals(ID_FUNCIONALIDAD, domain.getIdFuncionalidad());
        assertEquals(ID_TIPO_PARAMETRO, domain.getIdTipoParametro());
        assertTrue(domain.isActivo());
    }

    @Test
    void shouldConvertDtoRequestToDomainWithFalseActivo() {
        var dtoRequest = CrearParametroDtoRequest.create(NOMBRE, ID_FUNCIONALIDAD.toString(), ID_TIPO_PARAMETRO.toString(), "false");
        var domain = CrearParametroDtoMapper.INSTANCE.toDomain(dtoRequest);

        assertNotNull(domain);
        assertFalse(domain.isActivo());
    }

    @Test
    void shouldHandleNullDtoRequestInToDomain() {
        // Cuando se pasa null, el mapper crea un nuevo CrearParametroDtoRequest con valores por defecto
        // pero el constructor por defecto lanza excepcion porque nombre vacio no es valido
        assertThrows(ParametroException.class, () -> CrearParametroDtoMapper.INSTANCE.toDomain((CrearParametroDtoRequest) null));
    }

    // ==================== TESTS DE INTEGRACION ====================

    @Test
    void shouldCompleteMappingChainFromRequestToDomain() {
        var dtoRequest = CrearParametroDtoRequest.create(NOMBRE, ID_FUNCIONALIDAD.toString(), ID_TIPO_PARAMETRO.toString(), "true");
        var dtoInput = CrearParametroDtoMapper.INSTANCE.toDtoInput(dtoRequest);
        var domain = CrearParametroDtoMapper.INSTANCE.toDomain(dtoInput);

        assertEquals(NOMBRE, domain.getNombre());
        assertEquals(ID_FUNCIONALIDAD, domain.getIdFuncionalidad());
        assertEquals(ID_TIPO_PARAMETRO, domain.getIdTipoParametro());
        assertTrue(domain.isActivo());
    }
}
