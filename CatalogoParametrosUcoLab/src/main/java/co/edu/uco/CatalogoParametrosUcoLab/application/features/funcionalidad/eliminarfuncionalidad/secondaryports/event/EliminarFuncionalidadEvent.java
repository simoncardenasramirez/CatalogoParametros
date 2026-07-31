package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.eliminarfuncionalidad.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.secondaryports.event.FuncionalidadEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;

public final class EliminarFuncionalidadEvent implements FuncionalidadEvent {

    private FuncionalidadEntity funcionalidad;
    private EventType event;

    public enum EventType {
        DELETED
    }

    public EliminarFuncionalidadEvent(final FuncionalidadEntity funcionalidad, final EventType event) {
        setFuncionalidad(funcionalidad);
        setEvent(event);
    }

    public static EliminarFuncionalidadEvent deleted(final FuncionalidadEntity funcionalidad) {
        return new EliminarFuncionalidadEvent(funcionalidad, EventType.DELETED);
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