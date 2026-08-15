package co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.constants.Constants;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

class PropertiesHelperTest {

    @Test
    void debeRecuperarElValorSegunElArchivoYLaLlave() {
        assertEquals("La fecha final no tiene un formato valido (yyyy-MM-dd HH:mm:ss). Valor recibido:",
                PropertiesHelper.getValue(Constants.MESSAGE_PROPERTIES_FILE, "MSG-1"));
    }

    @Test
    void debeFallarCuandoLaLlaveNoEstaConfigurada() {
        assertThrows(NotFoundException.class,
                () -> PropertiesHelper.getValue(Constants.MESSAGE_PROPERTIES_FILE, "MSG-999"));
    }
}
