package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.metadatoambiente.crearmetadatoambiente;

import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.secondaryports.event.CrearMetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.secondaryports.publisher.CrearMetadatoAmbientePublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class CrearMetadatoAmbientePublisherImpl implements CrearMetadatoAmbientePublisher {
    private final Sinks.Many<CrearMetadatoAmbienteEvent> sink = Sinks.many().replay().limit(100);

    @Override
    public void sendEvent(final CrearMetadatoAmbienteEvent event) {
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Flux<CrearMetadatoAmbienteEvent> getStream() {
        return sink.asFlux();
    }
}
