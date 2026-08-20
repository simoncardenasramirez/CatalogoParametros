package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.funcionalidad.eliminarfuncionalidad;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.secondaryports.event.EliminarFuncionalidadEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;

class EliminarFuncionalidadPublisherImplTest {

    @Test
    void debeEmitirElEventoCuandoSePublica() {
        var publisher = new EliminarFuncionalidadPublisherImpl();
        var funcionalidad = FuncionalidadEntity.create(UUID.randomUUID(), "funcionalidad", UUID.randomUUID(), true,
                LocalDateTime.now(), null);
        var evento = EliminarFuncionalidadEvent.deleted(funcionalidad);

        publisher.sendEvent(evento);

        var emitido = publisher.getStream().blockFirst();
        assertEquals(evento, emitido);
    }

    @Test
    void debeDevolverUnFluxNoNuloCuandoSeObtieneElStream() {
        var publisher = new EliminarFuncionalidadPublisherImpl();

        assertNotNull(publisher.getStream());
    }
}