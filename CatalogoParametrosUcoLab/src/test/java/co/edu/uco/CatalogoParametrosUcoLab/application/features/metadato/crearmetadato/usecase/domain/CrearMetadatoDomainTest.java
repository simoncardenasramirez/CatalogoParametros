package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.usecase.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

class CrearMetadatoDomainTest {

    private static final String UUID_DEFAULT = "00000000-0000-0000-0000-000000000000";

    @Test
    void debeCrearConLosValoresCuandoLosDatosSonValidos() {
        var id = UUID.randomUUID();
        var idParametro = UUID.randomUUID();
        var idTipoMetadato = UUID.randomUUID();

        var dominio = CrearMetadatoDomain.create(id, idParametro, idTipoMetadato, "valor");

        assertEquals(id, dominio.getId());
        assertEquals(idParametro, dominio.getIdParametro());
        assertEquals(idTipoMetadato, dominio.getIdTipoMetadato());
        assertEquals("valor", dominio.getValor());
    }

    @Test
    void debeRecortarElValorCuandoTieneEspaciosAlInicioYAlFinal() {
        var dominio = CrearMetadatoDomain.create(UUID.randomUUID(), UUID.randomUUID(),
                UUID.randomUUID(), "  valor  ");

        assertEquals("valor", dominio.getValor());
    }

    @Test
    void debeAsignarElUuidPorDefectoCuandoElIdEsNulo() {
        var dominio = CrearMetadatoDomain.create(null, UUID.randomUUID(), UUID.randomUUID(), "valor");

        assertEquals(UUID.fromString(UUID_DEFAULT), dominio.getId());
    }

    @Test
    void debeAsignarElUuidPorDefectoCuandoElIdParametroEsNulo() {
        var dominio = CrearMetadatoDomain.create(UUID.randomUUID(), null, UUID.randomUUID(), "valor");

        assertEquals(UUID.fromString(UUID_DEFAULT), dominio.getIdParametro());
    }

    @Test
    void debeAsignarElUuidPorDefectoCuandoElIdTipoMetadatoEsNulo() {
        var dominio = CrearMetadatoDomain.create(UUID.randomUUID(), UUID.randomUUID(), null, "valor");

        assertEquals(UUID.fromString(UUID_DEFAULT), dominio.getIdTipoMetadato());
    }

    @Test
    void debeAsignarValorVacioCuandoElValorEsNulo() {
        var dominio = CrearMetadatoDomain.create(UUID.randomUUID(), UUID.randomUUID(),
                UUID.randomUUID(), null);

        assertEquals("", dominio.getValor());
    }

    @Test
    void debeGenerarUnIdNoNuloCuandoSeInvocaGenerateId() {
        var dominio = CrearMetadatoDomain.create(null, UUID.randomUUID(), UUID.randomUUID(), "valor");

        dominio.generateId();

        assertNotNull(dominio.getId());
        assertNotEquals(UUIDHelper.getDefault(), dominio.getId());
    }
}