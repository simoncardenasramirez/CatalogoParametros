package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.message;

import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.MensajeCambio;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message.MensajeCambioPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class MensajeCambioPublisherImpl implements MensajeCambioPublisher {

    private final Sinks.Many<MensajeCambio> sink = Sinks.many().multicast().directBestEffort();

    @Override
    public void sendEvent(final MensajeCambio event) {
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Flux<MensajeCambio> getStream() {
        return sink.asFlux();
    }
}
