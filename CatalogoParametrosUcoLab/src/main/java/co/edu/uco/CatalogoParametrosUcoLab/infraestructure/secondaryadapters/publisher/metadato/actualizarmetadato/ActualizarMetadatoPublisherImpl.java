package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.metadato.actualizarmetadato;
import org.springframework.stereotype.Component;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.secondaryports.event.ActualizarMetadatoEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.secondaryports.publisher.ActualizarMetadatoPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
@Component
public final class ActualizarMetadatoPublisherImpl implements ActualizarMetadatoPublisher {
    private final Sinks.Many<ActualizarMetadatoEvent> sink = Sinks.many().replay().limit(100);
    @Override public void sendEvent(final ActualizarMetadatoEvent event) { sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST); }
    @Override public Flux<ActualizarMetadatoEvent> getStream() { return sink.asFlux(); }
}
