package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.organizacion.crearorganizacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.secondaryports.event.CrearOrganizacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;

class CrearOrganizacionPublisherImplTest {

    private final CrearOrganizacionPublisherImpl publisher = new CrearOrganizacionPublisherImpl();

    @Test
    void debeEmitirElEventoCuandoSePublica() {
        var evento = CrearOrganizacionEvent.created(
                OrganizacionEntity.create(UUID.randomUUID(), "organizacion"));

        publisher.sendEvent(evento);

        var recibidos = publisher.getStream().take(1).collectList().block(Duration.ofSeconds(1));
        assertEquals(List.of(evento), recibidos);
    }

    @Test
    void debePermitirSuscribirseAlStreamCuandoNoSePublicanEventos() {
        var stream = publisher.getStream();
        assertNotNull(stream);
        var suscripcion = stream.subscribe();
        suscripcion.dispose();
    }
}