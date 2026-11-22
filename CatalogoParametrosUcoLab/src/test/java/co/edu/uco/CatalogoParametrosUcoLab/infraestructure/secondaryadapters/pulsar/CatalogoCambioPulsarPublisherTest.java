package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.pulsar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.ProducerBuilder;
import org.apache.pulsar.client.api.PulsarClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import reactor.core.publisher.Flux;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.CambioCatalogoAplicacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.IdentidadServicioEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.publisher.CambiosCatalogoAplicacionPublisher;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.FuncionalidadRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.IdentidadServicioRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.MetadatoRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ModuloRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.ParametroRepository;
import tools.jackson.databind.node.JsonNodeFactory;

@ExtendWith(MockitoExtension.class)
class CatalogoCambioPulsarPublisherTest {

    private static final String PREFIX = "persistent://public/default/catalogo-parametros-events";

    @Mock private CambiosCatalogoAplicacionPublisher cambios;
    @Mock private PulsarClient client;
    @Mock private ProducerBuilder<String> producerBuilder;
    @Mock private Producer<String> producer;
    @Mock private org.apache.pulsar.client.api.TypedMessageBuilder<String> messageBuilder;
    @Mock private MessageId messageId;
    @Mock private ParametroRepository parametros;
    @Mock private FuncionalidadRepository funcionalidades;
    @Mock private ModuloRepository modulos;
    @Mock private MetadatoAmbienteRepository metadatosAmbiente;
    @Mock private MetadatoRepository metadatos;
    @Mock private IdentidadServicioRepository identidades;

    private CatalogoCambioPulsarPublisher publisher;

    @BeforeEach
    void setUp() {
        publisher = new CatalogoCambioPulsarPublisher(cambios, client, PREFIX, false,
                parametros, funcionalidades, modulos, metadatosAmbiente, identidades, metadatos);
    }

    @Test
    void debeConstruirEventoDeParametroConContratoVersionado() {
        final UUID parametro = UUID.randomUUID();
        final UUID funcionalidad = UUID.randomUUID();
        final UUID aplicacion = UUID.randomUUID();
        final UUID ambiente = UUID.randomUUID();
        final String nombre = "correo";

        final var evento = publisher.construirEvento(
                new CambioCatalogoAplicacion("parametros", "UPDATED", parametro, funcionalidad, null, nombre),
                aplicacion, ambiente, "2026-11-21T10:00:00Z");

        assertEquals("catalogo-parametros", evento.source());
        assertEquals("ParameterUpdated", evento.eventType());
        assertEquals(parametro, evento.resourceId());
        assertEquals(nombre, evento.resourceName());
        assertEquals(funcionalidad, evento.parentId());
        assertEquals(aplicacion, evento.applicationId());
        assertEquals(ambiente, evento.environmentId());
        assertEquals(3, evento.schemaVersion());
        assertEquals("2026-11-21T10:00:00Z", evento.occurredAt());
    }

    @Test
    void debePublicarSoloEnElTopicDeLasIdentidadesDelMismoAmbienteYAplicacion() {
        final UUID aplicacion = UUID.randomUUID();
        final UUID ambienteDesarrollo = UUID.randomUUID();
        final UUID ambienteProduccion = UUID.randomUUID();
        final String clientDesarrollo = "svc-aplicacion-desarrollo";
        final String clientProduccion = "svc-aplicacion-produccion";

        when(identidades.findByIdAplicacion(aplicacion)).thenReturn(List.of(
                IdentidadServicioEntity.create(UUID.randomUUID(), aplicacion, ambienteDesarrollo, clientDesarrollo),
                IdentidadServicioEntity.create(UUID.randomUUID(), aplicacion, ambienteProduccion, clientProduccion),
                IdentidadServicioEntity.create(UUID.randomUUID(), UUID.randomUUID(), ambienteDesarrollo,
                        "svc-otra-aplicacion")));

        final Set<String> topics = publisher.topicsFor(aplicacion, ambienteDesarrollo);

        assertEquals(Set.of(PREFIX + "-" + clientDesarrollo), topics);
        assertTrue(topics.stream().noneMatch(topic -> topic.contains(clientProduccion)));
        assertTrue(topics.stream().noneMatch(topic -> topic.contains("otra-aplicacion")));
    }

    @Test
    void debeResolverTodosLosAmbientesAsociadosCuandoCambiaUnParametro() {
        final UUID parametro = UUID.randomUUID();
        final UUID ambienteUno = UUID.randomUUID();
        final UUID ambienteDos = UUID.randomUUID();

        when(metadatosAmbiente.findAll()).thenReturn(List.of(
                MetadatoAmbienteEntity.create(UUID.randomUUID(), parametro, ambienteUno, UUID.randomUUID()),
                MetadatoAmbienteEntity.create(UUID.randomUUID(), parametro, ambienteDos, UUID.randomUUID()),
                MetadatoAmbienteEntity.create(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID())));

        final Set<UUID> environments = publisher.environmentsFor(
                new CambioCatalogoAplicacion("parametros", "CREATED", parametro, UUID.randomUUID()));

        assertEquals(Set.of(ambienteUno, ambienteDos), environments);
    }

    @Test
    void debeConservarElAmbienteDelMetadatoCuandoConstruyeElEvento() {
        final UUID ambiente = UUID.randomUUID();
        final var evento = publisher.construirEvento(
                new CambioCatalogoAplicacion("metadatos", "DELETED", UUID.randomUUID(), UUID.randomUUID(), ambiente));

        assertEquals("MetadataDeleted", evento.eventType());
        assertEquals(ambiente, evento.environmentId());
        assertEquals("metadatos", evento.resource());
    }

    @Test
    void debeIncluirElValorNuevoCuandoElCambioLoTrae() {
        final var valor = JsonNodeFactory.instance.textNode("nuevo-valor");
        final var evento = publisher.construirEvento(
                new CambioCatalogoAplicacion("metadatos", "UPDATED", UUID.randomUUID(), UUID.randomUUID(),
                        UUID.randomUUID(), "correo", valor),
                UUID.randomUUID(), UUID.randomUUID(), "2026-11-21T10:00:00Z");

        assertEquals(valor, evento.resourceValue());
    }

    @Test
    void debeUsarElValorDelMetadatoCuandoElCambioNoLoIncluye() {
        final UUID parametro = UUID.randomUUID();
        final UUID ambiente = UUID.randomUUID();
        final var valor = JsonNodeFactory.instance.textNode("valor-consultado");

        when(metadatos.findByIdParametro(parametro)).thenReturn(List.of(
                MetadatoEntity.create(UUID.randomUUID(), parametro, UUID.randomUUID(), ambiente, valor)));

        final var evento = publisher.construirEvento(
                new CambioCatalogoAplicacion("parametros", "UPDATED", parametro, UUID.randomUUID()),
                UUID.randomUUID(), ambiente, "2026-11-21T10:00:00Z");

        assertEquals(valor, evento.resourceValue());
    }

    @Test
    void debeIncluirTopicDeAmbienteCuandoEstaHabilitado() {
        final UUID aplicacion = UUID.randomUUID();
        final UUID ambiente = UUID.randomUUID();
        final var publisherConTopicDeAmbiente = new CatalogoCambioPulsarPublisher(cambios, client, PREFIX, true,
                parametros, funcionalidades, modulos, metadatosAmbiente, identidades, metadatos);

        assertTrue(publisherConTopicDeAmbiente.topicsFor(aplicacion, ambiente)
                .contains(PREFIX + "-" + ambiente));
    }

    @Test
    void debeIgnorarIdentidadesInvalidasYErroresDelRepositorio() {
        final UUID aplicacion = UUID.randomUUID();
        final UUID ambiente = UUID.randomUUID();
        when(identidades.findByIdAplicacion(aplicacion)).thenReturn(List.of(
                IdentidadServicioEntity.create(UUID.randomUUID(), aplicacion, ambiente, "client valido"),
                IdentidadServicioEntity.create(UUID.randomUUID(), aplicacion, ambiente, "client-valido")));

        assertEquals(Set.of(PREFIX + "-client-valido"), publisher.topicsFor(aplicacion, ambiente));

        doThrow(new IllegalStateException("repositorio no disponible"))
                .when(identidades).findByIdAplicacion(aplicacion);
        assertTrue(publisher.topicsFor(aplicacion, ambiente).isEmpty());
    }

    @Test
    void debeRetornarVacioCuandoNoPuedeResolverElAmbiente() {
        assertTrue(publisher.topicsFor(null, UUID.randomUUID()).isEmpty());
        assertTrue(publisher.environmentsFor(
                new CambioCatalogoAplicacion("metadatos", "UPDATED", UUID.randomUUID(), UUID.randomUUID()))
                .isEmpty());
        assertNull(publisher.construirEvento(
                new CambioCatalogoAplicacion("otro", null, UUID.randomUUID(), UUID.randomUUID()))
                .applicationId());
    }

    @Test
    void debePublicarElCambioEnElTopicAutorizadoYCerrarRecursos() {
        final UUID aplicacion = UUID.randomUUID();
        final UUID funcionalidad = UUID.randomUUID();
        final UUID modulo = UUID.randomUUID();
        final UUID parametro = UUID.randomUUID();
        final UUID ambiente = UUID.randomUUID();
        final String topic = PREFIX + "-client-desarrollo";
        final var cambio = new CambioCatalogoAplicacion("parametros", "UPDATED", parametro, funcionalidad,
                null, "correo", JsonNodeFactory.instance.textNode("nuevo"));

        when(cambios.getStream()).thenReturn(Flux.just(cambio));
        when(funcionalidades.findById(funcionalidad)).thenReturn(Optional.of(
                co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity.create(
                        funcionalidad, "Correo", modulo, true, null, null)));
        when(modulos.findById(modulo)).thenReturn(Optional.of(
                co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity.create(
                        modulo, "Modulo", aplicacion, true, null, null)));
        when(metadatosAmbiente.findAll()).thenReturn(List.of(
                MetadatoAmbienteEntity.create(UUID.randomUUID(), parametro, ambiente, UUID.randomUUID())));
        when(identidades.findByIdAplicacion(aplicacion)).thenReturn(List.of(
                IdentidadServicioEntity.create(UUID.randomUUID(), aplicacion, ambiente, "client-desarrollo")));
        when(client.newProducer(org.apache.pulsar.client.api.Schema.STRING)).thenReturn(producerBuilder);
        when(producerBuilder.topic(topic)).thenReturn(producerBuilder);
        when(producerBuilder.createAsync()).thenReturn(CompletableFuture.completedFuture(producer));
        when(producer.newMessage()).thenReturn(messageBuilder);
        when(messageBuilder.key(parametro.toString())).thenReturn(messageBuilder);
        when(messageBuilder.value(org.mockito.ArgumentMatchers.anyString())).thenReturn(messageBuilder);
        when(messageBuilder.sendAsync()).thenReturn(CompletableFuture.completedFuture(messageId));
        when(producer.closeAsync()).thenReturn(CompletableFuture.completedFuture(null));

        publisher.iniciar();

        org.mockito.Mockito.verify(messageBuilder, org.mockito.Mockito.timeout(1000)).sendAsync();
        org.mockito.Mockito.verify(messageBuilder).key(parametro.toString());
        publisher.cerrar();
        org.mockito.Mockito.verify(producer).closeAsync();
    }
}
