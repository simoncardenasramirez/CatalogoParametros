package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.pulsar;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.CompletableFuture;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.ConsumerBuilder;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageListener;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.MensajeCambio;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.MensajeCambioPublisher;

@ExtendWith(MockitoExtension.class)
class MessageChangeListenerTest {

    @Mock private PulsarClient client;
    @Mock private ConsumerBuilder<String> builder;
    @Mock private Consumer<String> consumer;
    @Mock private MensajeCambioPublisher publisher;

    void configurarBuilder() {
        when(client.newConsumer(Schema.STRING)).thenReturn(builder);
        when(builder.topic(anyString())).thenReturn(builder);
        when(builder.subscriptionName(anyString())).thenReturn(builder);
        when(builder.messageListener(any())).thenReturn(builder);
        when(builder.subscribeAsync()).thenReturn(CompletableFuture.completedFuture(consumer));
    }

    @Test
    void debePublicarElAvisoCuandoElPayloadEsValido() {
        final var listener = new MessageChangeListener(null, publisher,
                "persistent://public/default/message-events", "catalogo-parametros", "svc-catalogo");

        listener.procesar("""
                {"eventType":"MessageUpdated","messageCode":"MSG_100","environmentId":"dev","eventId":"evt-1"}
                """);

        verify(publisher).sendEvent(new MensajeCambio("MessageUpdated", "MSG_100", "dev", "evt-1"));
    }

    @Test
    void debeRechazarElAvisoCuandoNoTieneTipoDeEvento() {
        final var listener = new MessageChangeListener(null, publisher,
                "persistent://public/default/message-events", "catalogo-parametros", "svc-catalogo");

        assertThrows(IllegalArgumentException.class, () -> listener.procesar("{\"messageCode\":\"MSG_100\"}"));
    }

    @Test
    void debeSuscribirseYConfirmarElAvisoValido() {
        configurarBuilder();
        final var listener = new MessageChangeListener(client, publisher,
                "persistent://public/default/message-events", "catalogo-parametros", "svc-catalogo");
        listener.iniciar();

        final ArgumentCaptor<MessageListener<String>> captor = ArgumentCaptor.forClass(MessageListener.class);
        verify(builder).messageListener(captor.capture());
        final Message<String> message = mock(Message.class);
        when(message.getValue()).thenReturn("""
                {"eventType":"MessageUpdated","messageCode":"MSG_100","environmentId":"dev","eventId":"evt-1"}
                """);

        captor.getValue().received(consumer, message);

        verify(consumer).acknowledgeAsync(message);
    }

    @Test
    void debeReintentarElAvisoInvalido() {
        configurarBuilder();
        final var listener = new MessageChangeListener(client, publisher,
                "persistent://public/default/message-events", "catalogo-parametros", "svc-catalogo");
        listener.iniciar();

        final ArgumentCaptor<MessageListener<String>> captor = ArgumentCaptor.forClass(MessageListener.class);
        verify(builder).messageListener(captor.capture());
        final Message<String> message = mock(Message.class);
        when(message.getValue()).thenReturn("{\"messageCode\":\"MSG_100\"}");

        captor.getValue().received(consumer, message);

        verify(consumer).negativeAcknowledge(message);
    }

    @Test
    void debeCerrarElConsumidorAlDetenerse() throws Exception {
        configurarBuilder();
        final var listener = new MessageChangeListener(client, publisher,
                "persistent://public/default/message-events", "catalogo-parametros", "svc-catalogo");
        listener.iniciar();

        listener.cerrar();

        verify(consumer).close();
    }
}
