package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.mensaje;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.message.ConsultarMensajeDetalleInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.MensajeCambio;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.MensajeCambioPublisher;
import reactor.core.publisher.Flux;

class MensajeEventosControllerTest {

    @Test
    void debePublicarElAvisoComoSseCuandoExisteUnCambio() {
        final var publisher = mock(MensajeCambioPublisher.class);
        when(publisher.getStream()).thenReturn(Flux.just(
                new MensajeCambio("MessageUpdated", "MSG_100", "dev", "evt-1")));
        final var controller = new MensajeController(
                mock(ConsultarMensajeDetalleInteractor.class), publisher);
        final var client = WebTestClient.bindToController(controller).build();

        client.get().uri("/catalogo-parametros/api/v1/mensajes/events")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith("text/event-stream")
                .expectBody(String.class)
                .value(body -> {
                    org.junit.jupiter.api.Assertions.assertTrue(body.contains(":connected"));
                    org.junit.jupiter.api.Assertions.assertTrue(body.contains("MessageUpdated"));
                    org.junit.jupiter.api.Assertions.assertTrue(body.contains("MSG_100"));
                });
    }
}
