package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.metadatoambiente.actualizarmetadatoambiente;

import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.secondaryports.event.ActualizarMetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.secondaryports.publisher.ActualizarMetadatoAmbientePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class ActualizarMetadatoAmbientePublisherImpl implements ActualizarMetadatoAmbientePublisher {
    private final Sinks.Many<ActualizarMetadatoAmbienteEvent> sink = Sinks.many().replay().limit(100);

    @Override
    public void sendEvent(final ActualizarMetadatoAmbienteEvent event) {
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Flux<ActualizarMetadatoAmbienteEvent> getStream() {
        return sink.asFlux();
    }
}
