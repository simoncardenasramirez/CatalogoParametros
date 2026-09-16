package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.primaryports.interactor.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.primaryports.dto.ActualizarMetadatoDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.primaryports.dto.ActualizarMetadatoDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.usecase.domain.ActualizarMetadatoDomain;
import tools.jackson.databind.node.JsonNodeFactory;

class ActualizarMetadatoDtoMapperTest {

    @Test
    void debeConvertirRequestEnDtoInputConTodosLosDatos() {
        var idParametro = UUID.randomUUID();
        var idTipoMetadato = UUID.randomUUID();
        var valor = JsonNodeFactory.instance.objectNode();
        valor.put("contenido", "valor actualizado");
        var request = ActualizarMetadatoDtoRequest.create(
                idParametro.toString(), idTipoMetadato.toString(), valor);

        ActualizarMetadatoDtoInput input = ActualizarMetadatoDtoMapper.INSTANCE.toDtoInput(request);

        assertEquals(idParametro, input.idParametro());
        assertEquals(idTipoMetadato, input.idTipoMetadato());
        assertEquals(valor, input.valor());
    }

    @Test
    void debeConvertirDtoInputEnDomainConservandoIdYCopiandoElValor() {
        var id = UUID.randomUUID();
        var idParametro = UUID.randomUUID();
        var idTipoMetadato = UUID.randomUUID();
        var valor = JsonNodeFactory.instance.objectNode();
        valor.put("contenido", "valor actualizado");
        var input = new ActualizarMetadatoDtoInput(idParametro, idTipoMetadato, valor);

        ActualizarMetadatoDomain domain = ActualizarMetadatoDtoMapper.INSTANCE.toDomain(id, input);

        assertEquals(id, domain.getId());
        assertEquals(idParametro, domain.getIdParametro());
        assertEquals(idTipoMetadato, domain.getIdTipoMetadato());
        assertEquals(valor, domain.getValor());
        assertNotSame(valor, domain.getValor());
    }
}
