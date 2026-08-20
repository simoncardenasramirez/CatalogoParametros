package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.interactor.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto.CrearParametroDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.primaryports.dto.CrearParametroDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.usecase.domain.CrearParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

class CrearParametroDtoMapperTest {

    @Test
    void debeConvertirRequestTodoStringEnInputConDatosReales() {
        var idFuncionalidad = UUID.randomUUID().toString();
        var idTipoParametro = UUID.randomUUID().toString();
        var request = CrearParametroDtoRequest.create("parametro", idFuncionalidad, idTipoParametro, "true");

        CrearParametroDtoInput input = CrearParametroDtoMapper.INSTANCE.toDtoInput(request);

        assertEquals("parametro", input.getNombre());
        assertEquals(UUID.fromString(idFuncionalidad), input.getIdFuncionalidad());
        assertEquals(UUID.fromString(idTipoParametro), input.getIdTipoParametro());
        assertTrue(input.isActivo());
    }

    @Test
    void debeConvertirInputEnDomainConIdPorDefecto() {
        var idFuncionalidad = UUID.randomUUID();
        var idTipoParametro = UUID.randomUUID();
        var input = CrearParametroDtoInput.create("parametro", idFuncionalidad, idTipoParametro, true);

        CrearParametroDomain domain = CrearParametroDtoMapper.INSTANCE.toDomain(input);

        assertEquals(UUIDHelper.getDefault(), domain.getId());
        assertEquals("parametro", domain.getNombre());
        assertEquals(idFuncionalidad, domain.getIdFuncionalidad());
        assertEquals(idTipoParametro, domain.getIdTipoParametro());
        assertTrue(domain.isActivo());
    }

    @Test
    void debeSerEquivalenteToDomainDeRequestYDeInput() {
        var request = CrearParametroDtoRequest.create("parametro", UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), "false");
        var viaRequest = CrearParametroDtoMapper.INSTANCE.toDomain(request);
        var viaInput = CrearParametroDtoMapper.INSTANCE.toDomain(CrearParametroDtoMapper.INSTANCE.toDtoInput(request));

        assertEquals(viaRequest.getNombre(), viaInput.getNombre());
        assertEquals(viaRequest.getIdFuncionalidad(), viaInput.getIdFuncionalidad());
        assertEquals(viaRequest.getIdTipoParametro(), viaInput.getIdTipoParametro());
        assertEquals(viaRequest.isActivo(), viaInput.isActivo());
    }

    @Test
    void debeLanzarValidationExceptionCuandoElRequestEsNulo() {
        assertThrows(ValidationException.class, () -> CrearParametroDtoMapper.INSTANCE.toDtoInput(null));
        assertThrows(ValidationException.class,
                () -> CrearParametroDtoMapper.INSTANCE.toDomain((CrearParametroDtoRequest) null));
    }
}