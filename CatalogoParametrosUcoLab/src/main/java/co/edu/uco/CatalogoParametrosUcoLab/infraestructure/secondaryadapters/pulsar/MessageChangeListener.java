package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.pulsar;

import java.util.concurrent.CompletableFuture;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.MensajeCambio;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.MensajeCambioPublisher;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

/** Consume los avisos del ambiente asociado a la identidad técnica del Catálogo. */
@Component
@ConditionalOnProperty(prefix = "pulsar", name = "enabled", havingValue = "true")
public final class MessageChangeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageChangeListener.class);

    private final PulsarClient client;
    private final MensajeCambioPublisher publisher;
    private final String topicPrefix;
    private final String subscriptionName;
    private final String clientId;
    private final JsonMapper mapper = JsonMapper.builder().build();
    private volatile Consumer<String> consumer;
    private volatile CompletableFuture<Consumer<String>> subscription;

    public MessageChangeListener(final PulsarClient client, final MensajeCambioPublisher publisher,
            @Value("${messageucolab.events-topic-prefix}") final String topicPrefix,
            @Value("${pulsar.subscription-name}") final String subscriptionName,
            @Value("${pulsar.client-id}") final String clientId) {
        this.client = client;
        this.publisher = publisher;
        this.topicPrefix = topicPrefix;
        this.subscriptionName = subscriptionName;
        this.clientId = clientId;
    }

    @jakarta.annotation.PostConstruct
    void iniciar() {
        if (clientId == null || clientId.isBlank()) {
            LOGGER.warn("[MESSAGEUCOLAB-PULSAR] No se inicia el listener: falta pulsar.client-id");
            return;
        }

        final String topic = topicPrefix + "-" + clientId;
        try {
            subscription = client.newConsumer(Schema.STRING)
                    .topic(topic)
                    .subscriptionName(subscriptionName)
                    .messageListener(this::recibir)
                    .subscribeAsync();
            subscription.whenComplete((suscriptor, error) -> {
                if (error != null) {
                    LOGGER.warn("[MESSAGEUCOLAB-PULSAR] No fue posible suscribirse a {}. "
                            + "El backend continuará disponible.", topic, error);
                    return;
                }
                consumer = suscriptor;
                LOGGER.info("[MESSAGEUCOLAB-PULSAR] Suscrito a {} con {}", topic, subscriptionName);
            });
        } catch (final RuntimeException exception) {
            LOGGER.warn("[MESSAGEUCOLAB-PULSAR] No fue posible iniciar el listener. "
                    + "El backend continuará disponible.", exception);
        }
    }

    private void recibir(final Consumer<String> consumer, final Message<String> message) {
        try {
            final MensajeCambio cambio = procesar(message.getValue());
            consumer.acknowledgeAsync(message);
            LOGGER.info("[MESSAGEUCOLAB-PULSAR] Evento consumido: tipo={}, codigo={}, ambiente={}, eventoId={}",
                    cambio.eventType(), cambio.messageCode(), cambio.environmentId(), cambio.eventId());
        } catch (final RuntimeException exception) {
            LOGGER.error("[MESSAGEUCOLAB-PULSAR] Aviso inválido; se reintentará el mensaje", exception);
            consumer.negativeAcknowledge(message);
        }
    }

    MensajeCambio procesar(final String payload) {
        final JsonNode aviso = mapper.readTree(payload);
        final String eventType = texto(aviso, "eventType");
        if (eventType.isBlank()) {
            throw new IllegalArgumentException("El aviso no contiene eventType");
        }

        final MensajeCambio cambio = new MensajeCambio(
                eventType,
                texto(aviso, "messageCode"),
                texto(aviso, "environmentId"),
                texto(aviso, "eventId"));
        publisher.sendEvent(cambio);
        return cambio;
    }

    private String texto(final JsonNode node, final String campo) {
        return node.path(campo).asText("");
    }

    @jakarta.annotation.PreDestroy
    void cerrar() {
        final Consumer<String> consumidor = consumer;
        if (consumidor != null) {
            try {
                consumidor.close();
            } catch (final PulsarClientException exception) {
                LOGGER.debug("[MESSAGEUCOLAB-PULSAR] Error cerrando el consumidor", exception);
            }
        }
    }
}
