package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.eliminarorganizacion.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.secondaryports.event.OrganizacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;

public final class EliminarOrganizacionEvent implements OrganizacionEvent {

    private OrganizacionEntity organizacion;
    private EventType event;

    public enum EventType {
        DELETED
    }

    public EliminarOrganizacionEvent(final OrganizacionEntity organizacion, final EventType event) {
        setOrganizacion(organizacion);
        setEvent(event);
    }

    public static EliminarOrganizacionEvent deleted(final OrganizacionEntity organizacion) {
        return new EliminarOrganizacionEvent(organizacion, EventType.DELETED);
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
