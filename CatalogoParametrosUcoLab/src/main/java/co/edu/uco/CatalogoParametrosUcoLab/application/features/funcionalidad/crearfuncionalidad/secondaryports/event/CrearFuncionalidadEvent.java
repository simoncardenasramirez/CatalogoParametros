package co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.crearfuncionalidad.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.funcionalidad.secondaryports.event.FuncionalidadEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;

public final class CrearFuncionalidadEvent implements FuncionalidadEvent {

    private FuncionalidadEntity funcionalidad;
    private EventType event;

    public enum EventType {
        CREATED
    }

    public CrearFuncionalidadEvent(final FuncionalidadEntity funcionalidad, final EventType event) {
        setFuncionalidad(funcionalidad);
        setEvent(event);
    }

    public static CrearFuncionalidadEvent created(final FuncionalidadEntity funcionalidad) {
        return new CrearFuncionalidadEvent(funcionalidad, EventType.CREATED);
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
