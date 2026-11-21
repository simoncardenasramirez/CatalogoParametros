package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.pulsar;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.CambioCatalogoAplicacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.IdentidadServicioEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.publisher.CambiosCatalogoAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.IdentidadServicioRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import reactor.core.Disposable;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.JsonNode;

/**
 * Publica los cambios del catalogo en Pulsar, aislando cada aviso por aplicacion, ambiente e
 * identidad de servicio. El consumidor se suscribe a {@code <prefijo>-<client_id>}, igual que
 * MessageUcoLab.
 */
@Component
@ConditionalOnProperty(prefix = "pulsar.publisher", name = "enabled", havingValue = "true")
public final class CatalogoCambioPulsarPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(CatalogoCambioPulsarPublisher.class);
    private static final String SOURCE = "catalogo-parametros";
    private static final Pattern SAFE_TOPIC_SEGMENT = Pattern.compile("[A-Za-z0-9_-]+");

    private final CambiosCatalogoAplicacionPublisher cambios;
    private final PulsarClient client;
    private final String eventsTopicPrefix;
    private final boolean environmentTopicEnabled;
    private final ParametroRepository parametros;
    private final FuncionalidadRepository funcionalidades;
    private final ModuloRepository modulos;
    private final MetadatoAmbienteRepository metadatosAmbiente;
    private final MetadatoRepository metadatos;
    private final IdentidadServicioRepository identidades;
    private final JsonMapper mapper = JsonMapper.builder().build();
    private final Map<String, CompletableFuture<Producer<String>>> producers = new ConcurrentHashMap<>();
    private Disposable subscription;

    @Autowired
    public CatalogoCambioPulsarPublisher(final CambiosCatalogoAplicacionPublisher cambios,
            @Qualifier("catalogoParametrosPulsarPublisherClient") final PulsarClient client,
            @Value("${pulsar.publisher.events-topic-prefix}") final String eventsTopicPrefix,
            @Value("${pulsar.publisher.environment-topic-enabled:false}") final boolean environmentTopicEnabled,
            final ParametroRepository parametros,
            final FuncionalidadRepository funcionalidades,
            final ModuloRepository modulos,
            final MetadatoAmbienteRepository metadatosAmbiente,
            final IdentidadServicioRepository identidades,
            final MetadatoRepository metadatos) {
        this.cambios = cambios;
        this.client = client;
        this.eventsTopicPrefix = eventsTopicPrefix;
        this.environmentTopicEnabled = environmentTopicEnabled;
        this.parametros = parametros;
        this.funcionalidades = funcionalidades;
        this.modulos = modulos;
        this.metadatosAmbiente = metadatosAmbiente;
        this.identidades = identidades;
        this.metadatos = metadatos;
    }

    /** Constructor reducido conservado para las pruebas del contrato externo. */
    CatalogoCambioPulsarPublisher(final CambiosCatalogoAplicacionPublisher cambios,
            final PulsarClient client, final String eventsTopicPrefix, final String ignoredDefaultEnvironmentId) {
        this(cambios, client, eventsTopicPrefix, false, null, null, null, null, null, null);
    }

    @jakarta.annotation.PostConstruct
    void iniciar() {
        if (cambios == null || client == null) {
            return;
        }
        subscription = cambios.getStream()
                .filter(this::esCambioPublicable)
                .subscribe(this::publicar,
                        exception -> LOGGER.error("[CATALOGO-PULSAR] Se detuvo el flujo de cambios", exception));
        LOGGER.info("[CATALOGO-PULSAR] Publicador listo con prefijo {}", eventsTopicPrefix);
    }

    private boolean esCambioPublicable(final CambioCatalogoAplicacion cambio) {
        return cambio != null && ("parametros".equals(cambio.recurso())
                || "metadatos".equals(cambio.recurso())
                || "metadatos-ambiente".equals(cambio.recurso()));
    }

    private void publicar(final CambioCatalogoAplicacion cambio) {
        try {
            final UUID applicationId = applicationId(cambio);
            if (applicationId == null) {
                LOGGER.warn("[CATALOGO-PULSAR] No se pudo resolver la aplicacion para recurso={} id={}",
                        cambio.recurso(), cambio.id());
                return;
            }

            final Set<UUID> environments = environmentsFor(cambio);
            if (environments.isEmpty()) {
                LOGGER.warn("[CATALOGO-PULSAR] No se pudo resolver ambiente para aplicacion={} recurso={} id={}",
                        applicationId, cambio.recurso(), cambio.id());
                return;
            }

            final String occurredAt = Instant.now().toString();
            environments.forEach(environmentId -> publicarEnDestino(cambio, applicationId, environmentId, occurredAt));
        } catch (final RuntimeException exception) {
            LOGGER.error("[CATALOGO-PULSAR] No fue posible preparar el evento recurso={} id={}",
                    cambio.recurso(), cambio.id(), exception);
        }
    }

    private void publicarEnDestino(final CambioCatalogoAplicacion cambio, final UUID applicationId,
            final UUID environmentId, final String occurredAt) {
        final CatalogoCambioPulsarEvent event = construirEvento(cambio, applicationId, environmentId, occurredAt);
        final String payload;
        try {
            payload = mapper.writeValueAsString(event);
        } catch (final RuntimeException exception) {
            LOGGER.error("[CATALOGO-PULSAR] No fue posible serializar el evento {}", event.eventId(), exception);
            return;
        }

        topicsFor(applicationId, environmentId).forEach(topic -> enviar(topic, event, payload));
    }

    /**
     * Devuelve los destinos autorizados. El topic de ambiente es opt-in por compatibilidad; el
     * aislamiento normal se hace exclusivamente mediante los client_id registrados para la
     * aplicacion y el ambiente del cambio.
     */
    Set<String> topicsFor(final UUID applicationId, final UUID environmentId) {
        if (applicationId == null || environmentId == null) {
            return Set.of();
        }

        final Set<String> topics = new LinkedHashSet<>();
        if (environmentTopicEnabled) {
            topics.add(topic(environmentId.toString()));
        }
        clientIdsOf(applicationId, environmentId).stream()
                .filter(this::safeTopicSegment)
                .map(this::topic)
                .forEach(topics::add);
        return topics;
    }

    private List<String> clientIdsOf(final UUID applicationId, final UUID environmentId) {
        if (identidades == null) {
            return List.of();
        }
        try {
            return identidades.findByIdAplicacion(applicationId).stream()
                    .filter(identity -> applicationId.equals(identity.getIdAplicacion()))
                    .filter(identity -> environmentId.equals(identity.getIdAmbiente()))
                    .map(IdentidadServicioEntity::getClientId)
                    .toList();
        } catch (final RuntimeException exception) {
            LOGGER.error("[CATALOGO-PULSAR] No se pudieron consultar las identidades de aplicacion={} ambiente={}",
                    applicationId, environmentId, exception);
            return List.of();
        }
    }

    private boolean safeTopicSegment(final String value) {
        return value != null && !value.isBlank() && SAFE_TOPIC_SEGMENT.matcher(value).matches();
    }

    private String topic(final String segment) {
        return eventsTopicPrefix + "-" + segment;
    }

    private void enviar(final String topic, final CatalogoCambioPulsarEvent event, final String payload) {
        try {
            productor(topic).thenCompose(producer -> producer.newMessage()
                    .key(event.resourceId().toString())
                    .value(payload)
                    .sendAsync())
                    .whenComplete((messageId, exception) -> {
                        if (exception != null) {
                            LOGGER.error("[CATALOGO-PULSAR] No fue posible publicar {} en {}",
                                    event.eventType(), topic, exception);
                        } else {
                            LOGGER.info("[CATALOGO-PULSAR] Evento publicado: tipo={}, recurso={}, id={}, aplicacion={}, ambiente={}, topic={}",
                                    event.eventType(), event.resource(), event.resourceId(), event.applicationId(),
                                    event.environmentId(), topic);
                        }
                    });
        } catch (final RuntimeException exception) {
            LOGGER.error("[CATALOGO-PULSAR] No fue posible crear productor en {}", topic, exception);
        }
    }

    private CompletableFuture<Producer<String>> productor(final String topic) {
        final CompletableFuture<Producer<String>> future = producers.computeIfAbsent(topic, this::crearProductor);
        future.whenComplete((producer, exception) -> {
            if (exception != null) {
                producers.remove(topic, future);
            }
        });
        return future;
    }

    private CompletableFuture<Producer<String>> crearProductor(final String topic) {
        try {
            return client.newProducer(Schema.STRING).topic(topic).createAsync();
        } catch (final RuntimeException exception) {
            return CompletableFuture.failedFuture(exception);
        }
    }

    Set<UUID> environmentsFor(final CambioCatalogoAplicacion cambio) {
        if (cambio.idAmbiente() != null) {
            return Set.of(cambio.idAmbiente());
        }
        if (!"parametros".equals(cambio.recurso()) || metadatosAmbiente == null) {
            return Set.of();
        }
        try {
            return metadatosAmbiente.findAll().stream()
                    .filter(entity -> cambio.id().equals(entity.getIdParametro()))
                    .map(MetadatoAmbienteEntity::getIdAmbiente)
                    .filter(id -> id != null)
                    .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));
        } catch (final RuntimeException exception) {
            LOGGER.error("[CATALOGO-PULSAR] No se pudieron consultar ambientes del parametro {}",
                    cambio.id(), exception);
            return Set.of();
        }
    }

    private UUID applicationId(final CambioCatalogoAplicacion cambio) {
        if ("parametros".equals(cambio.recurso())) {
            return applicationFromFunctionality(cambio.idPadre());
        }
        if ("metadatos".equals(cambio.recurso()) || "metadatos-ambiente".equals(cambio.recurso())) {
            return parametros == null ? null : parametros.findById(cambio.idPadre())
                    .map(ParametroEntity::getIdFuncionalidad)
                    .map(this::applicationFromFunctionality)
                    .orElse(null);
        }
        return null;
    }

    private UUID applicationFromFunctionality(final UUID functionalityId) {
        if (functionalityId == null || funcionalidades == null || modulos == null) {
            return null;
        }
        return funcionalidades.findById(functionalityId)
                .flatMap(functionality -> modulos.findById(functionality.getIdModulo()))
                .map(module -> module.getIdAplicacion())
                .orElse(null);
    }

    CatalogoCambioPulsarEvent construirEvento(final CambioCatalogoAplicacion cambio) {
        return construirEvento(cambio, null, cambio.idAmbiente(), Instant.now().toString());
    }

    CatalogoCambioPulsarEvent construirEvento(final CambioCatalogoAplicacion cambio,
            final UUID applicationId, final UUID environmentId, final String occurredAt) {
        return new CatalogoCambioPulsarEvent(
                SOURCE,
                eventType(cambio),
                cambio.recurso(),
                cambio.operacion(),
                cambio.nombreRecurso(),
                valorNuevo(cambio, environmentId),
                cambio.id(),
                cambio.idPadre(),
                applicationId,
                environmentId,
                UUID.randomUUID(),
                CatalogoCambioPulsarEvent.CURRENT_SCHEMA_VERSION,
                occurredAt);
    }

    private JsonNode valorNuevo(final CambioCatalogoAplicacion cambio, final UUID environmentId) {
        if (cambio.valorNuevo() != null) {
            return cambio.valorNuevo().deepCopy();
        }
        if (metadatos == null || environmentId == null) {
            return null;
        }
        final UUID parametroId = "parametros".equals(cambio.recurso()) ? cambio.id() : cambio.idPadre();
        if (parametroId == null) {
            return null;
        }
        return metadatos.findByIdParametro(parametroId).stream()
                .filter(metadato -> environmentId.equals(metadato.getIdAmbiente()))
                .map(metadato -> metadato.getValor() == null ? null : metadato.getValor().deepCopy())
                .filter(java.util.Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private String eventType(final CambioCatalogoAplicacion cambio) {
        final String resource = switch (cambio.recurso()) {
            case "parametros" -> "Parameter";
            case "metadatos" -> "Metadata";
            case "metadatos-ambiente" -> "MetadataEnvironment";
            default -> "Catalog";
        };
        final String operation = cambio.operacion() == null ? "Changed" : cambio.operacion().toLowerCase();
        return resource + Character.toUpperCase(operation.charAt(0)) + operation.substring(1);
    }

    @jakarta.annotation.PreDestroy
    void cerrar() {
        if (subscription != null) {
            subscription.dispose();
        }
        producers.values().forEach(future -> future.thenCompose(Producer::closeAsync)
                .exceptionally(exception -> null));
        producers.clear();
    }
}
