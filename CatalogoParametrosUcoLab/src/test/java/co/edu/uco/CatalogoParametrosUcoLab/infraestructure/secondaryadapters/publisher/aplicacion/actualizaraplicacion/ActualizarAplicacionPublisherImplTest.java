package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.aplicacion.actualizaraplicacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Duration;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.secondaryports.event.ActualizarAplicacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;

class ActualizarAplicacionPublisherImplTest {

    private ActualizarAplicacionEvent evento() {
        var entidad = AplicacionEntity.create(UUID.randomUUID(), "aplicacion", UUID.randomUUID(), true, null, null);
        return ActualizarAplicacionEvent.updated(entidad);
    }

    @Test
    void debeEmitirElEventoCuandoSeEnviaUno() {
        var publisher = new ActualizarAplicacionPublisherImpl();
        var evento = evento();

        publisher.sendEvent(evento);

        var recibido = publisher.getStream().blockFirst(Duration.ofSeconds(1));
        assertNotNull(recibido);
        assertEquals(evento, recibido);
    }

    @Test
    void debeEmitirLosEventosEnElMismoOrdenEnQueSeEnvian() {
        var publisher = new ActualizarAplicacionPublisherImpl();
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