package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.pulsar;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.CambioCatalogoAplicacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.publisher.CambiosCatalogoAplicacionPublisher;
import reactor.core.Disposable;
import tools.jackson.databind.json.JsonMapper;

/** Publica invalidaciones del catalogo para MessageUcoLab, Security u otros consumidores. */
@Component
@ConditionalOnProperty(prefix = "pulsar.publisher", name = "enabled", havingValue = "true")
public final class CatalogoCambioPulsarPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogoCambioPulsarPublisher.class);
    private static final String SOURCE = "catalogo-parametros";

    private final CambiosCatalogoAplicacionPublisher cambios;
    private final PulsarClient client;
    private final String topicNames;
    private final UUID defaultEnvironmentId;
    private final JsonMapper mapper = JsonMapper.builder().build();
    private final Map<String, Producer<String>> producers = new LinkedHashMap<>();
    private Disposable subscription;

    public CatalogoCambioPulsarPublisher(final CambiosCatalogoAplicacionPublisher cambios,
            final PulsarClient client,
            @Value("${pulsar.publisher.topic-names}") final String topicNames,
            @Value("${pulsar.publisher.default-environment-id:}") final String defaultEnvironmentId) {
        this.cambios = cambios;
        this.client = client;
        this.topicNames = topicNames;
        this.defaultEnvironmentId = parseUuid(defaultEnvironmentId);
    }

    @jakarta.annotation.PostConstruct
    void iniciar() {
        for (final String topic : configuredTopics()) {
            try {
                producers.put(topic, client.newProducer(Schema.STRING)
                        .topic(topic)
                        .producerName(SOURCE)
                        .create());
                LOGGER.info("[CATALOGO-PULSAR] Productor listo en {}", topic);
            } catch (final PulsarClientException | RuntimeException exception) {
                LOGGER.warn("[CATALOGO-PULSAR] No fue posible crear productor en {}. "
                        + "Se omitira ese destino.", topic, exception);
            }
        }

        if (producers.isEmpty()) {
            LOGGER.warn("[CATALOGO-PULSAR] No hay topics de publicacion configurados; "
                    + "los cambios del catalogo no saldran por Pulsar.");
            return;
        }

        subscription = cambios.getStream()
                .filter(this::esCambioPublicable)
                .subscribe(this::publicar, exception -> LOGGER.error(
                        "[CATALOGO-PULSAR] Se detuvo el flujo de cambios del catalogo", exception));
    }

    private boolean esCambioPublicable(final CambioCatalogoAplicacion cambio) {
        return "parametros".equals(cambio.recurso()) || "metadatos".equals(cambio.recurso());
    }

    private void publicar(final CambioCatalogoAplicacion cambio) {
        final CatalogoCambioPulsarEvent event = construirEvento(cambio);
        final String payload = mapper.writeValueAsString(event);

        producers.forEach((topic, producer) -> producer.newMessage()
                .key(event.eventId().toString())
                .value(payload)
                .sendAsync()
                .whenComplete((messageId, exception) -> {
                    if (exception != null) {
                        LOGGER.error("[CATALOGO-PULSAR] No fue posible publicar {} en {}",
                                event.eventType(), topic, exception);
                    } else {
                        LOGGER.info("[CATALOGO-PULSAR] Evento publicado: tipo={}, recurso={}, id={}, topic={}",
                                event.eventType(), event.resource(), event.resourceId(), topic);
                    }
                }));
    }

    CatalogoCambioPulsarEvent construirEvento(final CambioCatalogoAplicacion cambio) {
        final UUID eventId = UUID.randomUUID();
        return new CatalogoCambioPulsarEvent(
                SOURCE,
                eventType(cambio),
                cambio.recurso(),
                cambio.operacion(),
                cambio.id(),
                cambio.idPadre(),
                cambio.idAmbiente() == null ? defaultEnvironmentId : cambio.idAmbiente(),
                eventId);
    }

    private String eventType(final CambioCatalogoAplicacion cambio) {
        final String recurso = "parametros".equals(cambio.recurso()) ? "Parameter" : "Metadata";
        final String operacion = cambio.operacion().toLowerCase(Locale.ROOT);
        return recurso + Character.toUpperCase(operacion.charAt(0)) + operacion.substring(1);
    }

    private String[] configuredTopics() {
        return Arrays.stream(topicNames == null ? new String[0] : topicNames.split(","))
                .map(String::trim)
                .filter(topic -> !topic.isBlank())
                .distinct()
                .toArray(String[]::new);
    }

    private UUID parseUuid(final String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return UUID.fromString(value.trim());
        } catch (final IllegalArgumentException exception) {
            LOGGER.warn("[CATALOGO-PULSAR] CATALOGO_ENVIRONMENT_ID no es un UUID valido; "
                    + "se publicara el ambiente de cada metadato.");
            return null;
        }
    }

    @jakarta.annotation.PreDestroy
    void cerrar() {
        if (subscription != null) {
            subscription.dispose();
        }
        producers.values().forEach(producer -> {
            try {
                producer.close();
            } catch (final PulsarClientException exception) {
                LOGGER.debug("[CATALOGO-PULSAR] Error cerrando productor", exception);
            }
        });
    }
}
