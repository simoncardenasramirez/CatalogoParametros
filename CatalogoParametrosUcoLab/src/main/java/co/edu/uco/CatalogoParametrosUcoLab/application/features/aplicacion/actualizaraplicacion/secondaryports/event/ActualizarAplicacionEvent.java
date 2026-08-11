package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.actualizaraplicacion.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.secondaryports.event.AplicacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;

public final class ActualizarAplicacionEvent implements AplicacionEvent {

    private AplicacionEntity aplicacion;
    private EventType event;

    public enum EventType {
        UPDATED
    }

    public ActualizarAplicacionEvent(final AplicacionEntity aplicacion, final EventType event) {
        setAplicacion(aplicacion);
        setEvent(event);
    }

    public static ActualizarAplicacionEvent updated(final AplicacionEntity aplicacion) {
        return new ActualizarAplicacionEvent(aplicacion, EventType.UPDATED);
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
