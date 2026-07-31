package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.publisher.funcionalidad.actualizarfuncionalidad;

import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.secondaryports.event.ActualizarFuncionalidadEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.secondaryports.publisher.ActualizarFuncionalidadPublisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public final class ActualizarFuncionalidadPublisherImpl implements ActualizarFuncionalidadPublisher {

    private final Sinks.Many<ActualizarFuncionalidadEvent> sink = Sinks.many().multicast().onBackpressureBuffer();

    @Override
    public void sendEvent(final ActualizarFuncionalidadEvent event) {
        sink.tryEmitNext(event);
    }

    @Override
    public Flux<ActualizarFuncionalidadEvent> getStream() {
        return sink.asFlux();
    }
}
