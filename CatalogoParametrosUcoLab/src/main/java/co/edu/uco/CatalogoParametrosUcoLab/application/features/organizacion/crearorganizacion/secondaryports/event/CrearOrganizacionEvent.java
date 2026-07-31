package co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.crearorganizacion.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.organizacion.secondaryports.event.OrganizacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;

public final class CrearOrganizacionEvent implements OrganizacionEvent {

    private OrganizacionEntity organizacion;
    private EventType event;

    public enum EventType {
        CREATED
    }

    public CrearOrganizacionEvent(final OrganizacionEntity organizacion, final EventType event) {
        setOrganizacion(organizacion);
        setEvent(event);
    }

    public static CrearOrganizacionEvent created(final OrganizacionEntity organizacion) {
        return new CrearOrganizacionEvent(organizacion, EventType.CREATED);
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
