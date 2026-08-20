package co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.UUIDHelper;

class DomainTest {

    private static final String UUID_DEFAULT = "00000000-0000-0000-0000-000000000000";

    private Domain dominioConId(final UUID id) {
        return new Domain(id) {
        };
    }

    @Test
    void debeAsignarElUuidPorDefectoCuandoElIdEsNulo() {
        var dominio = dominioConId(null);
        assertEquals(UUID.fromString(UUID_DEFAULT), dominio.getId());
    }

    @Test
    void debeConservarElUuidCuandoElIdNoEsNulo() {
        var uuid = UUID.randomUUID();
        var dominio = dominioConId(uuid);
        assertEquals(uuid, dominio.getId());
    }

    @Test
    void debeGenerarUnUuidNoNuloCuandoSeInvocaGenerateId() {
        var dominio = dominioConId(null);
        dominio.generateId();
        assertNotNull(dominio.getId());
        assertNotEquals(UUIDHelper.getDefault(), dominio.getId());
    }

    @Test
    void debeCambiarElUuidCuandoSeInvocaGenerateIdSobreUnIdExistente() {
        var uuid = UUID.randomUUID();
        var dominio = dominioConId(uuid);
        dominio.generateId();
        assertNotEquals(uuid, dominio.getId());
    }
}