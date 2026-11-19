package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.mensaje;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.message.ConsultarMensajeDetalleInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.MensajeDetalle;

class MensajeControllerTest {

    private final ConsultarMensajeDetalleInteractor interactor = org.mockito.Mockito.mock(
            ConsultarMensajeDetalleInteractor.class);

    private final WebTestClient webTestClient = WebTestClient.bindToController(
            new MensajeController(interactor)).build();

    @Test
    void debeEntregarElDetalleDelMensajeCuandoElCodigoExiste() {
        when(interactor.execute("MSG-100"))
                .thenReturn(new MensajeDetalle("MSG_100", "Validación del nombre",
                        "El nombre es obligatorio.", "Funcional", "Advertencia", "Organizaciones"));

        webTestClient.get()
                .uri("/catalogo-parametros/api/v1/mensajes/MSG-100")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.code").isEqualTo("MSG_100")
                .jsonPath("$.title").isEqualTo("Validación del nombre")
                .jsonPath("$.content").isEqualTo("El nombre es obligatorio.");
    }
}
