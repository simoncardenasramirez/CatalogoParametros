package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.funcionalidad.actualizarfuncionalidad;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.secondaryports.event.ActualizarFuncionalidadEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;

class ActualizarFuncionalidadPublisherImplTest {

    @Test
    void debeEmitirElEventoCuandoSePublica() {
        var publisher = new ActualizarFuncionalidadPublisherImpl();
        var funcionalidad = FuncionalidadEntity.create(UUID.randomUUID(), "funcionalidad", UUID.randomUUID(), true,
                OffsetDateTime.now(ZoneOffset.of("-05:00")), null);
        var evento = ActualizarFuncionalidadEvent.updated(funcionalidad);

        publisher.sendEvent(evento);

        var emitido = publisher.getStream().blockFirst();
        assertEquals(evento, emitido);
    }

    @Test
    void debeDevolverUnFluxNoNuloCuandoSeObtieneElStream() {
        var publisher = new ActualizarFuncionalidadPublisherImpl();

        assertNotNull(publisher.getStream());
    }
}