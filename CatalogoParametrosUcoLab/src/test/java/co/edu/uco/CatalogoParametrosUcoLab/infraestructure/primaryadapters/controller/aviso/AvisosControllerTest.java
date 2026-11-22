package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.controller.aviso;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.reactive.server.WebTestClient;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.MensajeCambio;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.MensajeCambioPublisher;
import reactor.core.publisher.Flux;

class AvisosControllerTest {

    @Test
    void debePublicarElAvisoConElNombreDelEventoDeLaGuia() {
        final var publisher = mock(MensajeCambioPublisher.class);
        when(publisher.getStream()).thenReturn(Flux.just(
                new MensajeCambio("MessageUpdated", "MSG_100", "dev", "evt-1")));
        final var client = WebTestClient.bindToController(new AvisosController(publisher)).build();

        client.get().uri("/catalogo-parametros/api/v1/avisos")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith("text/event-stream")
                .expectBody(String.class)
                .value(body -> {
                    org.junit.jupiter.api.Assertions.assertTrue(body.contains(":connected"));
                    org.junit.jupiter.api.Assertions.assertTrue(body.contains("event:aviso"));
                    org.junit.jupiter.api.Assertions.assertTrue(body.contains("MSG_100"));
                });
    }
}
