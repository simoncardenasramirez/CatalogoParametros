package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.interactor.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.dto.ActualizarParametroDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.primaryports.dto.ActualizarParametroDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.usecase.domain.ActualizarParametroDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.ValidationException;

class ActualizarParametroDtoMapperTest {

    @Test
    void debeConvertirRequestTodoStringEnInputConDatosReales() {
        var idFuncionalidad = UUID.randomUUID().toString();
        var idTipoParametro = UUID.randomUUID().toString();
        var request = ActualizarParametroDtoRequest.create("parametro", idFuncionalidad, idTipoParametro, "true");

        ActualizarParametroDtoInput input = ActualizarParametroDtoMapper.INSTANCE.toDtoInput(request);

        assertEquals("parametro", input.getNombre());
        assertEquals(UUID.fromString(idFuncionalidad), input.getIdFuncionalidad());
        assertEquals(UUID.fromString(idTipoParametro), input.getIdTipoParametro());
        assertTrue(input.isActivo());
    }

    @Test
    void debeConvertirInputEnDomainConservandoElId() {
        var id = UUID.randomUUID();
        var idFuncionalidad = UUID.randomUUID();
        var idTipoParametro = UUID.randomUUID();
        var input = ActualizarParametroDtoInput.create("parametro", idFuncionalidad, idTipoParametro, true);

        ActualizarParametroDomain domain = ActualizarParametroDtoMapper.INSTANCE.toDomain(id, input);

        assertEquals(id, domain.getId());
        assertEquals("parametro", domain.getNombre());
        assertEquals(idFuncionalidad, domain.getIdFuncionalidad());
        assertEquals(idTipoParametro, domain.getIdTipoParametro());
        assertTrue(domain.isActivo());
    }

    @Test
    void debeSerEquivalenteToDomainDeRequestYDeInput() {
        var id = UUID.randomUUID();
        var request = ActualizarParametroDtoRequest.create("parametro", UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), "false");
        var viaRequest = ActualizarParametroDtoMapper.INSTANCE.toDomain(id, request);
        var viaInput = ActualizarParametroDtoMapper.INSTANCE.toDomain(id,
                ActualizarParametroDtoMapper.INSTANCE.toDtoInput(request));

        assertEquals(viaRequest.getId(), viaInput.getId());
        assertEquals(viaRequest.getNombre(), viaInput.getNombre());
        assertEquals(viaRequest.getIdFuncionalidad(), viaInput.getIdFuncionalidad());
        assertEquals(viaRequest.getIdTipoParametro(), viaInput.getIdTipoParametro());
        assertEquals(viaRequest.isActivo(), viaInput.isActivo());
    }

    @Test
    void debeLanzarValidationExceptionCuandoElRequestEsNulo() {
        assertThrows(ValidationException.class, () -> ActualizarParametroDtoMapper.INSTANCE.toDtoInput(null));
        assertThrows(ValidationException.class,
                () -> ActualizarParametroDtoMapper.INSTANCE
                        .toDomain(UUID.randomUUID(), (ActualizarParametroDtoRequest) null));
    }
}