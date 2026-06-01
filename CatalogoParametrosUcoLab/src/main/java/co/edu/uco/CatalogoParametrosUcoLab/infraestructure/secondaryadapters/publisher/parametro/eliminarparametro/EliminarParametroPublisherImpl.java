package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.parametro.eliminarparametro;

import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.secondaryports.event.EliminarParametroEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.secondaryports.publisher.EliminarParametroPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class EliminarParametroPublisherImpl implements EliminarParametroPublisher {

    private final Sinks.Many<EliminarParametroEvent> sink = Sinks.many().multicast().onBackpressureBuffer();

    @Override
    public void sendEvent(final EliminarParametroEvent event) {
        sink.tryEmitNext(event);
    }

    @Override
    public Flux<EliminarParametroEvent> getStream() {
        return sink.asFlux();
    }
}
