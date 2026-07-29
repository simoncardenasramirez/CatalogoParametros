package co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.crearaplicacion.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.aplicacion.secondaryports.event.AplicacionEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;

public final class CrearAplicacionEvent implements AplicacionEvent {

    private AplicacionEntity aplicacion;
    private EventType event;

    public enum EventType {
        CREATED
    }

    public CrearAplicacionEvent(final AplicacionEntity aplicacion, final EventType event) {
        setAplicacion(aplicacion);
        setEvent(event);
    }

    public static CrearAplicacionEvent created(final AplicacionEntity aplicacion) {
        return new CrearAplicacionEvent(aplicacion, EventType.CREATED);
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
