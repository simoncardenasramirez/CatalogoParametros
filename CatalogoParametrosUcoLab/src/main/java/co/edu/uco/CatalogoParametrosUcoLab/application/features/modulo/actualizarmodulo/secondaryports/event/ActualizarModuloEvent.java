package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.actualizarmodulo.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.secondaryports.event.ModuloEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;

public final class ActualizarModuloEvent implements ModuloEvent {

    private ModuloEntity modulo;
    private EventType event;

    public enum EventType {
        UPDATED
    }

    public ActualizarModuloEvent(final ModuloEntity modulo, final EventType event) {
        setModulo(modulo);
        setEvent(event);
    }

    public static ActualizarModuloEvent updated(final ModuloEntity modulo) {
        return new ActualizarModuloEvent(modulo, EventType.UPDATED);
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
