package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.parametro.eliminarparametro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.secondaryports.event.EliminarParametroEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;

class EliminarParametroPublisherImplTest {

    @Test
    void debeEmitirElEventoCuandoSePublica() {
        var publisher = new EliminarParametroPublisherImpl();
        var entity = ParametroEntity.create(UUID.randomUUID(), "parametro", UUID.randomUUID(),
                UUID.randomUUID(), true);
        var event = EliminarParametroEvent.deleted(entity);

        publisher.sendEvent(event);

        var emitido = publisher.getStream().blockFirst();
        assertEquals(event, emitido);
    }

    @Test
    void debeDevolverUnFluxNoNuloCuandoSeSolicitaElStream() {
        var publisher = new EliminarParametroPublisherImpl();

        assertNotNull(publisher.getStream());
    }
}