package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.pulsar;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.MensajeCambio;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.MensajeCambioPublisher;

class MessageChangeListenerTest {

    @Test
    void debePublicarElAvisoCuandoElPayloadEsValido() {
        final MensajeCambioPublisher publisher = mock(MensajeCambioPublisher.class);
        final var listener = new MessageChangeListener(null, publisher,
                "persistent://public/default/message-events", "catalogo-parametros", "svc-catalogo");

        listener.procesar("""
                {"eventType":"MessageUpdated","messageCode":"MSG_100","environmentId":"dev","eventId":"evt-1"}
                """);

        verify(publisher).sendEvent(new MensajeCambio("MessageUpdated", "MSG_100", "dev", "evt-1"));
    }

    @Test
    void debeRechazarElAvisoCuandoNoTieneTipoDeEvento() {
        final MensajeCambioPublisher publisher = mock(MensajeCambioPublisher.class);
        final var listener = new MessageChangeListener(null, publisher,
                "persistent://public/default/message-events", "catalogo-parametros", "svc-catalogo");

        assertThrows(IllegalArgumentException.class, () -> listener.procesar("{\"messageCode\":\"MSG_100\"}"));
    }
}
