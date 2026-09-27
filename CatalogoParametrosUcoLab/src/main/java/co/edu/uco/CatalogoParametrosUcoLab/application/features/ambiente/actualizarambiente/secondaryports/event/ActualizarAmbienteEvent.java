package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.actualizarambiente.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.secondaryports.event.AmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;

public final class ActualizarAmbienteEvent implements AmbienteEvent {
    private AmbienteEntity ambiente;
    private EventType event;

    public enum EventType { UPDATED }

    public ActualizarAmbienteEvent(final AmbienteEntity ambiente, final EventType event) {
        setAmbiente(ambiente);
        setEvent(event);
    }

    public static ActualizarAmbienteEvent updated(final AmbienteEntity ambiente) {
        return new ActualizarAmbienteEvent(ambiente, EventType.UPDATED);
    }

    public AmbienteEntity getAmbiente() { return ambiente; }
    public void setAmbiente(final AmbienteEntity value) { ambiente = value; }
    public EventType getEvent() { return event; }
    public void setEvent(final EventType value) { event = value; }
}
