package co.edu.uco.CatalogoParametrosUcoLab.application.features.token;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config.BCryptTokenHashService;

class BCryptTokenHashServiceTest {
    private final BCryptTokenHashService service = new BCryptTokenHashService();

    @Test
    void debeGenerarSaltDiferenteYValidarElToken() {
        var primerHash = service.hash("token-secreto");
        var segundoHash = service.hash("token-secreto");

        assertNotEquals(primerHash, segundoHash);
        assertTrue(service.matches("token-secreto", primerHash));
        assertTrue(service.matches("token-secreto", segundoHash));
        assertFalse(service.matches("token-incorrecto", primerHash));
    }
}
