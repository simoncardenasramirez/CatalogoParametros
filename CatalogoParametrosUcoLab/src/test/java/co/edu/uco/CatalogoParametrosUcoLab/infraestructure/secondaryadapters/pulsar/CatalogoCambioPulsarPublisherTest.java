package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.pulsar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.CambioCatalogoAplicacion;

class CatalogoCambioPulsarPublisherTest {

    @Test
    void debeConstruirEventoDeParametroCuandoCambiaUnParametro() {
        final UUID parametro = UUID.randomUUID();
        final UUID funcionalidad = UUID.randomUUID();
        final UUID ambientePorDefecto = UUID.randomUUID();
        final var publicador = new CatalogoCambioPulsarPublisher(null, null, "topic", ambientePorDefecto.toString());

        final var evento = publicador.construirEvento(
                new CambioCatalogoAplicacion("parametros", "UPDATED", parametro, funcionalidad));

        assertEquals("catalogo-parametros", evento.source());
        assertEquals("ParameterUpdated", evento.eventType());
        assertEquals(parametro, evento.resourceId());
        assertEquals(funcionalidad, evento.parentId());
        assertEquals(ambientePorDefecto, evento.environmentId());
    }

    @Test
    void debeConservarElAmbienteDelMetadatoCuandoConstruyeElEvento() {
        final UUID ambiente = UUID.randomUUID();
        final var publicador = new CatalogoCambioPulsarPublisher(null, null, "topic", "");

        final var evento = publicador.construirEvento(
                new CambioCatalogoAplicacion("metadatos", "DELETED", UUID.randomUUID(), UUID.randomUUID(), ambiente));

        assertEquals("MetadataDeleted", evento.eventType());
        assertEquals(ambiente, evento.environmentId());
        assertEquals("metadatos", evento.resource());
    }
}
