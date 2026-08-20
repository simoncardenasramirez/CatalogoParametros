package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.message;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

class ConsultarMensajeAdapterTest {

    private final ConsultarMensajeAdapter adapter = new ConsultarMensajeAdapter();

    @Test
    void debeDevolverElTextoRealCuandoExisteLaLlave() {
        assertEquals("El nombre de la organizacion es obligatorio.",
                adapter.consultarMensaje("MSG-100"));
    }

    @Test
    void debeDevolverElMensajeConPlaceholdersCuandoLaLlaveContieneFormato() {
        assertEquals("El campo '%s' debe ser de tipo %s.",
                adapter.consultarMensaje("MSG-143"));
    }

    @Test
    void debeLanzarNotFoundExceptionCuandoLaLlaveNoExiste() {
        assertThrows(NotFoundException.class, () -> adapter.consultarMensaje("MSG-999"));
    }
}