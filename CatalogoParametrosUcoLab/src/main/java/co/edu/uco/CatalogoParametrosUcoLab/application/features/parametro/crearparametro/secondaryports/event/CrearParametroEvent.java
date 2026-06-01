package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.crearparametro.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.secondaryports.event.ParametroEvent;

public final class CrearParametroEvent implements ParametroEvent {

    private ParametroEntity parametro;
    private EventType event;

    public enum EventType {
        CREATED
    }

    public CrearParametroEvent(final ParametroEntity parametro, final EventType event) {
        setParametro(parametro);
        setEvent(event);
    }

    public static CrearParametroEvent created(final ParametroEntity parametro) {
        return new CrearParametroEvent(parametro, EventType.CREATED);
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
