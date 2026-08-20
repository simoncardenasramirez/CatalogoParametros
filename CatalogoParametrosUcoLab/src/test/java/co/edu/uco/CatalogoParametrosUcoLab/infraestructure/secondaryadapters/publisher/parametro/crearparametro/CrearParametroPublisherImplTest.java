package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.parametro.crearparametro;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.secondaryports.event.CrearParametroEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;

class CrearParametroPublisherImplTest {

    @Test
    void debeEmitirElEventoCuandoSePublica() {
        var publisher = new CrearParametroPublisherImpl();
        var entity = ParametroEntity.create(UUID.randomUUID(), "parametro", UUID.randomUUID(),
                UUID.randomUUID(), true);
        var event = CrearParametroEvent.created(entity);

        publisher.sendEvent(event);

        var emitido = publisher.getStream().blockFirst();
        assertEquals(event, emitido);
    }

    @Test
    void debeDevolverUnFluxNoNuloCuandoSeSolicitaElStream() {
        var publisher = new CrearParametroPublisherImpl();

        assertNotNull(publisher.getStream());
    }
}