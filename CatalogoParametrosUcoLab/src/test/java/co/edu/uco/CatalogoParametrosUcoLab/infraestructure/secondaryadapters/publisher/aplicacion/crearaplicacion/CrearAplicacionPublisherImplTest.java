package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.aplicacion.crearaplicacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Duration;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.secondaryports.event.CrearAplicacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;

class CrearAplicacionPublisherImplTest {

    private CrearAplicacionEvent evento() {
        var entidad = AplicacionEntity.create(UUID.randomUUID(), "aplicacion", UUID.randomUUID(), true, null, null);
        return CrearAplicacionEvent.created(entidad);
    }

    @Test
    void debeEmitirElEventoCuandoSeEnviaUno() {
        var publisher = new CrearAplicacionPublisherImpl();
        var evento = evento();

        publisher.sendEvent(evento);

        var recibido = publisher.getStream().blockFirst(Duration.ofSeconds(1));
        assertNotNull(recibido);
        assertEquals(evento, recibido);
    }

    @Test
    void debeEmitirLosEventosEnElMismoOrdenEnQueSeEnvian() {
        var publisher = new CrearAplicacionPublisherImpl();
        var eventoUno = evento();
        var eventoDos = evento();

        publisher.sendEvent(eventoUno);
        publisher.sendEvent(eventoDos);

        var recibidos = publisher.getStream().take(2).collectList().block(Duration.ofSeconds(1));
        assertNotNull(recibidos);
        assertEquals(2, recibidos.size());
        assertEquals(eventoUno, recibidos.get(0));
        assertEquals(eventoDos, recibidos.get(1));
    }
}