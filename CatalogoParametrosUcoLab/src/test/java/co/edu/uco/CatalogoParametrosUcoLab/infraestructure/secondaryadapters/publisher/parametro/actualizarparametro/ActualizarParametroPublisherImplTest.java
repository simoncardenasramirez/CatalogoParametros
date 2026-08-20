package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.parametro.actualizarparametro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.secondaryports.event.ActualizarParametroEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;

class ActualizarParametroPublisherImplTest {

    @Test
    void debeEmitirElEventoCuandoSePublica() {
        var publisher = new ActualizarParametroPublisherImpl();
        var entity = ParametroEntity.create(UUID.randomUUID(), "parametro", UUID.randomUUID(),
                UUID.randomUUID(), true);
        var event = ActualizarParametroEvent.updated(entity);

        publisher.sendEvent(event);

        var emitido = publisher.getStream().blockFirst();
        assertEquals(event, emitido);
    }

    @Test
    void debeDevolverUnFluxNoNuloCuandoSeSolicitaElStream() {
        var publisher = new ActualizarParametroPublisherImpl();

        assertNotNull(publisher.getStream());
    }
}