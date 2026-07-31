package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.secondaryports.publisher;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.secondaryports.event.EliminarOrganizacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.publisher.Publisher;
import reactor.core.publisher.Flux;

public interface EliminarOrganizacionPublisher extends Publisher<EliminarOrganizacionEvent> {
    void sendEvent(EliminarOrganizacionEvent event);
    Flux<EliminarOrganizacionEvent> getStream();
}
