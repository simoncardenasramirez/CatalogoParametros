package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.primaryports.dto.CrearMetadatosTodosAmbientesDtoInput;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.primaryports.dto.CrearMetadatosTodosAmbientesDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.usecase.domain.CrearMetadatosTodosAmbientesDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain.CargaMasivaLibro;
import tools.jackson.databind.ObjectMapper;

class CrearMetadatosTodosAmbientesDtoTest {
    @Test
    void debeNormalizarIdentificadoresYCopiarElValorEnRequest() {
        var parametro = UUID.randomUUID();
        var tipo = UUID.randomUUID();
        var valor = new ObjectMapper().createObjectNode().put("clave", "valor");

        var request = CrearMetadatosTodosAmbientesDtoRequest.create("  " + parametro + "  ", tipo.toString(), valor);

        assertEquals(parametro.toString(), request.getIdParametro());
        assertEquals(tipo.toString(), request.getIdTipoMetadato());
        assertEquals("valor", request.getValor().get("clave").asString());
        assertNotSame(valor, request.getValor());

        request.setValor(null);
        assertTrue(request.getValor().isNull());
    }

    @Test
    void debeCrearInputDominioYLoteConValoresNulosSinRomperLaInmutabilidad() {
        var parametro = UUID.randomUUID();
        var tipo = UUID.randomUUID();
        var valor = new ObjectMapper().createObjectNode().put("activo", true);

        var input = CrearMetadatosTodosAmbientesDtoInput.create(parametro, tipo, valor);
        var dominio = CrearMetadatosTodosAmbientesDomain.create(parametro, tipo, valor);
        var lote = new CargaMasivaLibro(null, null);

        assertEquals(parametro, input.getIdParametro());
        assertEquals(tipo, input.getIdTipoMetadato());
        assertEquals("true", dominio.getValor().get("activo").asString());
        assertTrue(lote.filas().isEmpty());
        assertTrue(lote.errores().isEmpty());
    }
}
