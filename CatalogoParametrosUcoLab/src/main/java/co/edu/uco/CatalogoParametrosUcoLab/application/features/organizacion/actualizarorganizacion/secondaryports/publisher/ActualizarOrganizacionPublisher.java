package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.secondaryports.publisher;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.secondaryports.event.ActualizarOrganizacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.publisher.Publisher;
import reactor.core.publisher.Flux;

public interface ActualizarOrganizacionPublisher extends Publisher<ActualizarOrganizacionEvent> {
    void sendEvent(ActualizarOrganizacionEvent event);
    Flux<ActualizarOrganizacionEvent> getStream();
}
