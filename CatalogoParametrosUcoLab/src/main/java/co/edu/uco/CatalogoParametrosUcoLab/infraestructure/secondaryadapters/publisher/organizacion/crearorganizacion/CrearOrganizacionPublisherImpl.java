package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.organizacion.crearorganizacion;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.secondaryports.publisher.CrearOrganizacionPublisher;
import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.secondaryports.event.CrearOrganizacionEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class CrearOrganizacionPublisherImpl implements CrearOrganizacionPublisher {

    private final Sinks.Many<CrearOrganizacionEvent> sink = Sinks.many().replay().limit(100);

    @Override
    public void sendEvent(final CrearOrganizacionEvent event) {
        sink.emitNext(event, Sinks.EmitFailureHandler.FAIL_FAST);
    }

    @Override
    public Flux<CrearOrganizacionEvent> getStream() {
        return sink.asFlux();
    }
}
