package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.organizacion.actualizarorganizacion;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.secondaryports.publisher.ActualizarOrganizacionPublisher;
import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.secondaryports.event.ActualizarOrganizacionEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class ActualizarOrganizacionPublisherImpl implements ActualizarOrganizacionPublisher {

    private final Sinks.Many<ActualizarOrganizacionEvent> sink = Sinks.many().replay().limit(100);

    @Override
    public void sendEvent(final ActualizarOrganizacionEvent event) {
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Flux<ActualizarOrganizacionEvent> getStream() {
        return sink.asFlux();
    }
}
