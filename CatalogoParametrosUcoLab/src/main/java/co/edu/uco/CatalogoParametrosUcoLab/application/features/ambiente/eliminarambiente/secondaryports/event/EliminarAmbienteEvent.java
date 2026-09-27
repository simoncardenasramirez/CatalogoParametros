package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.eliminarambiente.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.secondaryports.event.AmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;

public final class EliminarAmbienteEvent implements AmbienteEvent {
    private AmbienteEntity ambiente;
    private EventType event;

    public enum EventType { DELETED }

    public EliminarAmbienteEvent(final AmbienteEntity ambiente, final EventType event) {
        setAmbiente(ambiente);
        setEvent(event);
    }

    public static EliminarAmbienteEvent deleted(final AmbienteEntity ambiente) {
        return new EliminarAmbienteEvent(ambiente, EventType.DELETED);
    }

    public AmbienteEntity getAmbiente() { return ambiente; }
    public void setAmbiente(final AmbienteEntity value) { ambiente = value; }
    public EventType getEvent() { return event; }
    public void setEvent(final EventType value) { event = value; }
}
