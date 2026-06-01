package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.parametro.actualizarparametro;

import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.secondaryports.event.ActualizarParametroEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.secondaryports.publisher.ActualizarParametroPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class ActualizarParametroPublisherImpl implements ActualizarParametroPublisher {

    private final Sinks.Many<ActualizarParametroEvent> sink = Sinks.many().multicast().onBackpressureBuffer();

    @Override
    public void sendEvent(final ActualizarParametroEvent event) {
        sink.tryEmitNext(event);
    }

    @Override
    public Flux<ActualizarParametroEvent> getStream() {
        return sink.asFlux();
    }
}
