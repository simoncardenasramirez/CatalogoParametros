package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.modulo.crearmodulo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.secondaryports.event.CrearModuloEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;

class CrearModuloPublisherImplTest {

    @Test
    void debeEmitirElEventoCuandoSePublica() {
        var publisher = new CrearModuloPublisherImpl();
        var modulo = ModuloEntity.create(UUID.randomUUID(), "modulo", UUID.randomUUID(), true,
                OffsetDateTime.now(ZoneOffset.of("-05:00")), null);
        var evento = CrearModuloEvent.created(modulo);

        publisher.sendEvent(evento);

        var emitido = publisher.getStream().blockFirst();
        assertEquals(evento, emitido);
    }

    @Test
    void debeDevolverUnFluxNoNuloCuandoSeObtieneElStream() {
        var publisher = new CrearModuloPublisherImpl();

        assertNotNull(publisher.getStream());
    }
}