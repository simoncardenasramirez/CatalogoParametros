package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.metadato.crearmetadato;

import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.secondaryports.event.CrearMetadatoEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.secondaryports.publisher.CrearMetadatoPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class CrearMetadatoPublisherImpl implements CrearMetadatoPublisher {
    private final Sinks.Many<CrearMetadatoEvent> sink = Sinks.many().replay().limit(100);
    @Override public void sendEvent(final CrearMetadatoEvent event) { sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST); }
    @Override public Flux<CrearMetadatoEvent> getStream() { return sink.asFlux(); }
}
