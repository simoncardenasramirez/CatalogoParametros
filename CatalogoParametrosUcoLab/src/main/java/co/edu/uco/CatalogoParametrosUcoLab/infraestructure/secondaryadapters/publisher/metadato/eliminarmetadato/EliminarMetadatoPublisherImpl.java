package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.metadato.eliminarmetadato;

import org.springframework.stereotype.Component;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.eliminarmetadato.secondaryports.event.EliminarMetadatoEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.eliminarmetadato.secondaryports.publisher.EliminarMetadatoPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
@Component
public final class EliminarMetadatoPublisherImpl implements EliminarMetadatoPublisher {
    private final Sinks.Many<EliminarMetadatoEvent> sink = Sinks.many().replay().limit(100);
    @Override public void sendEvent(final EliminarMetadatoEvent event) { sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST); }
    @Override public Flux<EliminarMetadatoEvent> getStream() { return sink.asFlux(); }
}
