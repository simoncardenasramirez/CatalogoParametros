package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.actualizarorganizacion.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.secondaryports.event.OrganizacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;

public final class ActualizarOrganizacionEvent implements OrganizacionEvent {

    private OrganizacionEntity organizacion;
    private EventType event;

    public enum EventType {
        UPDATED
    }

    public ActualizarOrganizacionEvent(final OrganizacionEntity organizacion, final EventType event) {
        setOrganizacion(organizacion);
        setEvent(event);
    }

    public static ActualizarOrganizacionEvent updated(final OrganizacionEntity organizacion) {
        return new ActualizarOrganizacionEvent(organizacion, EventType.UPDATED);
    }

    public OrganizacionEntity getOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(final OrganizacionEntity organizacion) {
        this.organizacion = organizacion;
    }

    public EventType getEvent() {
        return event;
    }

    public void setEvent(final EventType event) {
        this.event = event;
    }
}
