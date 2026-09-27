package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.metadatoambiente.eliminarmetadatoambiente;

import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.eliminarmetadatoambiente.secondaryports.event.EliminarMetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.eliminarmetadatoambiente.secondaryports.publisher.EliminarMetadatoAmbientePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class EliminarMetadatoAmbientePublisherImpl implements EliminarMetadatoAmbientePublisher {
    private final Sinks.Many<EliminarMetadatoAmbienteEvent> sink = Sinks.many().replay().limit(100);

    @Override
    public void sendEvent(final EliminarMetadatoAmbienteEvent event) {
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Flux<EliminarMetadatoAmbienteEvent> getStream() {
        return sink.asFlux();
    }
}
