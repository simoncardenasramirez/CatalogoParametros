package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.organizacion.actualizarorganizacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.secondaryports.event.ActualizarOrganizacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;

class ActualizarOrganizacionPublisherImplTest {

    private final ActualizarOrganizacionPublisherImpl publisher = new ActualizarOrganizacionPublisherImpl();

    @Test
    void debeEmitirElEventoCuandoSePublica() {
        var evento = ActualizarOrganizacionEvent.updated(
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