package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.modulo.eliminarmodulo;

import org.springframework.stereotype.Component;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.eliminarmodulo.secondaryports.event.EliminarModuloEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.eliminarmodulo.secondaryports.publisher.EliminarModuloPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
@Component
public final class EliminarModuloPublisherImpl implements EliminarModuloPublisher {
    private final Sinks.Many<EliminarModuloEvent> sink = Sinks.many().replay().limit(100);
    @Override public void sendEvent(final EliminarModuloEvent event) { sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST); }
    @Override public Flux<EliminarModuloEvent> getStream() { return sink.asFlux(); }
}
