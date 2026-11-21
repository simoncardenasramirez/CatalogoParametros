package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.pulsar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.apache.pulsar.client.api.PulsarClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.CambioCatalogoAplicacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.IdentidadServicioEntity;
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
}
