package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.cargamasiva;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain.CargaMasivaError;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain.CargaMasivaResultado;

class CargaMasivaResponseTest {
    @Test
    void debeMapearResultadoListoYMensajeDeExito() {
        var response = CargaMasivaResponse.from(new CargaMasivaResultado(true, 2, 2, 0, List.of()));
        assertTrue(response.isReady());
        assertEquals("El archivo esta listo para cargar.", response.getMensajes().getFirst());
    }

    @Test
    void debeMapearErroresYMensajeDeBloqueo() {
        var response = CargaMasivaResponse.from(new CargaMasivaResultado(false, 1, 0, 1,
                List.of(new CargaMasivaError(2, "Catalogo", "Aplicacion", "", "REQUIRED", "Obligatorio."))));
        assertEquals(1, response.getErrores().size());
        assertEquals("El archivo contiene errores y no puede cargarse.", response.getMensajes().getFirst());
    }
}
