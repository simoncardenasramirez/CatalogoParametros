package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.crearmodulo.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.secondaryports.event.ModuloEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;

public final class CrearModuloEvent implements ModuloEvent {

    private ModuloEntity modulo;
    private EventType event;

    public enum EventType {
        CREATED
    }

    public CrearModuloEvent(final ModuloEntity modulo, final EventType event) {
        setModulo(modulo);
        setEvent(event);
    }

    public static CrearModuloEvent created(final ModuloEntity modulo) {
        return new CrearModuloEvent(modulo, EventType.CREATED);
    }

    public ModuloEntity getModulo() {
        return modulo;
    }

    public void setModulo(final ModuloEntity modulo) {
        this.modulo = modulo;
    }

    public EventType getEvent() {
        return event;
    }

    public void setEvent(final EventType event) {
        this.event = event;
    }
}
