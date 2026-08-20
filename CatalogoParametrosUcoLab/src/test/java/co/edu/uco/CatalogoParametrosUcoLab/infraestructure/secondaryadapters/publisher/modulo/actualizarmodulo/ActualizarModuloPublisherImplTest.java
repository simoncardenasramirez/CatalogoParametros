package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.modulo.actualizarmodulo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.secondaryports.event.ActualizarModuloEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;

class ActualizarModuloPublisherImplTest {

    @Test
    void debeEmitirElEventoCuandoSePublica() {
        var publisher = new ActualizarModuloPublisherImpl();
        var modulo = ModuloEntity.create(UUID.randomUUID(), "modulo", UUID.randomUUID(), true,
                LocalDateTime.now(), null);
        var evento = ActualizarModuloEvent.updated(modulo);

        publisher.sendEvent(evento);

        var emitido = publisher.getStream().blockFirst();
        assertEquals(evento, emitido);
    }

    @Test
    void debeDevolverUnFluxNoNuloCuandoSeObtieneElStream() {
        var publisher = new ActualizarModuloPublisherImpl();

        assertNotNull(publisher.getStream());
    }
}