package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.eliminarparametro.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.secondaryports.event.ParametroEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;

public final class EliminarParametroEvent implements ParametroEvent {

    private ParametroEntity parametro;
    private EventType event;

    public enum EventType {
        DELETED
    }

    public EliminarParametroEvent(final ParametroEntity parametro, final EventType event) {
        setParametro(parametro);
        setEvent(event);
    }

    public static EliminarParametroEvent deleted(final ParametroEntity parametro) {
        return new EliminarParametroEvent(parametro, EventType.DELETED);
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
