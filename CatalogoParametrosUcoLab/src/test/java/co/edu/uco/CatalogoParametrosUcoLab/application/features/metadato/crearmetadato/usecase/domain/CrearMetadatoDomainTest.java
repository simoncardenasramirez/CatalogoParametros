package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;
import tools.jackson.databind.node.JsonNodeFactory;

class CrearMetadatoDomainTest {

    private static final String UUID_DEFAULT = "00000000-0000-0000-0000-000000000000";

    @Test
    void debeCrearConLosValoresCuandoLosDatosSonValidos() {
        var id = UUID.randomUUID();
        var idParametro = UUID.randomUUID();
        var idTipoMetadato = UUID.randomUUID();

        var dominio = CrearMetadatoDomain.create(id, idParametro, idTipoMetadato, text("valor"));

        assertEquals(id, dominio.getId());
        assertEquals(idParametro, dominio.getIdParametro());
        assertEquals(idTipoMetadato, dominio.getIdTipoMetadato());
        assertEquals("valor", dominio.getValor().asString());
    }

    @Test
    void debeConservarElValorJsonRecibido() {
        var dominio = CrearMetadatoDomain.create(UUID.randomUUID(), UUID.randomUUID(),
                UUID.randomUUID(), text("  valor  "));

        assertEquals("  valor  ", dominio.getValor().asString());
    }

    @Test
    void debeAsignarElUuidPorDefectoCuandoElIdEsNulo() {
        var dominio = CrearMetadatoDomain.create(null, UUID.randomUUID(), UUID.randomUUID(), text("valor"));

        assertEquals(UUID.fromString(UUID_DEFAULT), dominio.getId());
    }

    @Test
    void debeAsignarElUuidPorDefectoCuandoElIdParametroEsNulo() {
        var dominio = CrearMetadatoDomain.create(UUID.randomUUID(), null, UUID.randomUUID(), text("valor"));

        assertEquals(UUID.fromString(UUID_DEFAULT), dominio.getIdParametro());
    }

    @Test
    void debeAsignarElUuidPorDefectoCuandoElIdTipoMetadatoEsNulo() {
        var dominio = CrearMetadatoDomain.create(UUID.randomUUID(), UUID.randomUUID(), null, text("valor"));

        assertEquals(UUID.fromString(UUID_DEFAULT), dominio.getIdTipoMetadato());
    }

    @Test
    void debeAsignarNodoNuloCuandoElValorEsNulo() {
        var dominio = CrearMetadatoDomain.create(UUID.randomUUID(), UUID.randomUUID(),
                UUID.randomUUID(), null);

        assertEquals(true, dominio.getValor().isNull());
    }

    @Test
    void debeGenerarUnIdNoNuloCuandoSeInvocaGenerateId() {
        var dominio = CrearMetadatoDomain.create(null, UUID.randomUUID(), UUID.randomUUID(), text("valor"));

        dominio.generateId();

        assertNotNull(dominio.getId());
        assertNotEquals(UUIDHelper.getDefault(), dominio.getId());
    }

    private tools.jackson.databind.JsonNode text(final String value) {
        return JsonNodeFactory.instance.stringNode(value);
    }
}
