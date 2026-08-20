package co.edu.uco.CatalogoParametrosUcoLab.crosscutting.constants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

class ConstantsTest {

    @Test
    void debeDefinirElNombreDelArchivoDeMensajes() {
        assertEquals("message.properties", Constants.MESSAGE_PROPERTIES_FILE);
    }

    @Test
    void debeDefinirElNombreDelArchivoDeMensajesSinEstarVacio() {
        assertFalse(TextHelper.isBlank(Constants.MESSAGE_PROPERTIES_FILE));
    }
}