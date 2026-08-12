package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.eliminaraplicacion.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.secondaryports.event.AplicacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;

public final class EliminarAplicacionEvent implements AplicacionEvent {

    private AplicacionEntity aplicacion;
    private EventType event;

    public enum EventType {
        DELETED
    }

    public EliminarAplicacionEvent(final AplicacionEntity aplicacion, final EventType event) {
        setAplicacion(aplicacion);
        setEvent(event);
    }

    public static EliminarAplicacionEvent deleted(final AplicacionEntity aplicacion) {
        return new EliminarAplicacionEvent(aplicacion, EventType.DELETED);
    }

    public AplicacionEntity getAplicacion() {
        return aplicacion;
    }

    public void setAplicacion(final AplicacionEntity aplicacion) {
        this.aplicacion = aplicacion;
    }

    public EventType getEvent() {
        return event;
    }

    public void setEvent(final EventType event) {
        this.event = event;
    }
}
