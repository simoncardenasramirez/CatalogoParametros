package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.primaryports.interactor.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.primaryports.dto.CrearMetadatoDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.primaryports.dto.CrearMetadatoDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.domain.CrearMetadatoDomain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;
import tools.jackson.databind.node.JsonNodeFactory;

class CrearMetadatoDtoMapperTest {

    @Test
    void debeConvertirRequestEnDtoInputConTodosLosDatos() {
        var idParametro = UUID.randomUUID();
        var idTipoMetadato = UUID.randomUUID();
        var valor = JsonNodeFactory.instance.objectNode();
        valor.put("contenido", "valor");
        var request = CrearMetadatoDtoRequest.create(
                idParametro.toString(), idTipoMetadato.toString(), valor);

        CrearMetadatoDtoInput input = CrearMetadatoDtoMapper.INSTANCE.toDtoInput(request);

        assertEquals(idParametro, input.getIdParametro());
        assertEquals(idTipoMetadato, input.getIdTipoMetadato());
        assertEquals(valor, input.getValor());
    }

    @Test
    void debeConvertirDtoInputEnDomainConIdPorDefectoYCopiaDelValor() {
        var idParametro = UUID.randomUUID();
        var idTipoMetadato = UUID.randomUUID();
        var valor = JsonNodeFactory.instance.objectNode();
        valor.put("contenido", "valor");
        var input = CrearMetadatoDtoInput.create(idParametro, idTipoMetadato, valor);

        CrearMetadatoDomain domain = CrearMetadatoDtoMapper.INSTANCE.toDomain(input);

        assertEquals(UUIDHelper.getDefault(), domain.getId());
        assertEquals(idParametro, domain.getIdParametro());
        assertEquals(idTipoMetadato, domain.getIdTipoMetadato());
        assertEquals(valor, domain.getValor());
        assertNotSame(valor, domain.getValor());
    }
}
