package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.funcionalidad.crearfuncionalidad;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.secondaryports.event.CrearFuncionalidadEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;

class CrearFuncionalidadPublisherImplTest {

    @Test
    void debeEmitirElEventoCuandoSePublica() {
        var publisher = new CrearFuncionalidadPublisherImpl();
        var funcionalidad = FuncionalidadEntity.create(UUID.randomUUID(), "funcionalidad", UUID.randomUUID(), true,
                LocalDateTime.now(), null);
        var evento = CrearFuncionalidadEvent.created(funcionalidad);

        publisher.sendEvent(evento);

        var emitido = publisher.getStream().blockFirst();
        assertEquals(evento, emitido);
    }

    @Test
    void debeDevolverUnFluxNoNuloCuandoSeObtieneElStream() {
        var publisher = new CrearFuncionalidadPublisherImpl();

        assertNotNull(publisher.getStream());
    }
}