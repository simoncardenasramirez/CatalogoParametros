package co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class UUIDHelperTest {

    private static final String UUID_DEFAULT = "00000000-0000-0000-0000-000000000000";

    @Test
    void debeGenerarUnUuidNoNulo() {
        assertNotNull(UUIDHelper.generate());
    }

    @Test
    void debeGenerarUnUuidDiferenteEnCadaLlamada() {
        assertNotEquals(UUIDHelper.generate(), UUIDHelper.generate());
    }

    @Test
    void debeDevolverElUuidPorDefectoCuandoSeUsaLaSobreCargaSinArgumentos() {
        assertEquals(UUID.fromString(UUID_DEFAULT), UUIDHelper.getDefault());
    }

    @Test
    void debeDevolverElUuidPorDefectoCuandoElValorEsNulo() {
        assertEquals(UUID.fromString(UUID_DEFAULT), UUIDHelper.getDefault(null));
    }

    @Test
    void debeDevolverElMismoValorCuandoElUuidNoEsNulo() {
        var uuid = UUID.randomUUID();
        assertSame(uuid, UUIDHelper.getDefault(uuid));
    }
}