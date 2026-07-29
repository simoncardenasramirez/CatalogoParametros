package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.actualizarfuncionalidad.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.secondaryports.event.FuncionalidadEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;

public final class ActualizarFuncionalidadEvent implements FuncionalidadEvent {

    private FuncionalidadEntity funcionalidad;
    private EventType event;

    public enum EventType {
        UPDATED
    }

    public ActualizarFuncionalidadEvent(final FuncionalidadEntity funcionalidad, final EventType event) {
        setFuncionalidad(funcionalidad);
        setEvent(event);
    }

    public static ActualizarFuncionalidadEvent updated(final FuncionalidadEntity funcionalidad) {
        return new ActualizarFuncionalidadEvent(funcionalidad, EventType.UPDATED);
    }

    public FuncionalidadEntity getFuncionalidad() {
        return funcionalidad;
    }

    public void setFuncionalidad(final FuncionalidadEntity funcionalidad) {
        this.funcionalidad = funcionalidad;
    }

    public EventType getEvent() {
        return event;
    }

    public void setEvent(final EventType event) {
        this.event = event;
    }
}