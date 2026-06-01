package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.actualizarparametro.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.secondaryports.event.ParametroEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;

public final class ActualizarParametroEvent implements ParametroEvent {

    private ParametroEntity parametro;
    private EventType event;

    public enum EventType {
        UPDATED
    }

    public ActualizarParametroEvent(final ParametroEntity parametro, final EventType event) {
        setParametro(parametro);
        setEvent(event);
    }

    public static ActualizarParametroEvent updated(final ParametroEntity parametro) {
        return new ActualizarParametroEvent(parametro, EventType.UPDATED);
    }

    public ParametroEntity getParametro() {
        return parametro;
    }

    public void setParametro(final ParametroEntity parametro) {
        this.parametro = parametro;
    }

    public EventType getEvent() {
        return event;
    }

    public void setEvent(final EventType event) {
        this.event = event;
    }
}
