package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.aplicacion.eliminaraplicacion;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.secondaryports.publisher.EliminarAplicacionPublisher;
import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.secondaryports.event.EliminarAplicacionEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class EliminarAplicacionPublisherImpl implements EliminarAplicacionPublisher {

    private final Sinks.Many<EliminarAplicacionEvent> sink = Sinks.many().replay().limit(100);

    @Override
    public void sendEvent(final EliminarAplicacionEvent event) {
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Flux<EliminarAplicacionEvent> getStream() {
        return sink.asFlux();
    }
}
