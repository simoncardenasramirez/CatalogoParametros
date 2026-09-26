package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.crearambiente.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.secondaryports.event.AmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;

public final class CrearAmbienteEvent implements AmbienteEvent {
    private AmbienteEntity ambiente;
    private EventType event;

    public enum EventType { CREATED }

    public CrearAmbienteEvent(final AmbienteEntity ambiente, final EventType event) {
        setAmbiente(ambiente);
        setEvent(event);
    }

    public static CrearAmbienteEvent created(final AmbienteEntity ambiente) {
        return new CrearAmbienteEvent(ambiente, EventType.CREATED);
    }

    public AmbienteEntity getAmbiente() { return ambiente; }
    public void setAmbiente(final AmbienteEntity value) { ambiente = value; }
    public EventType getEvent() { return event; }
    public void setEvent(final EventType value) { event = value; }
}
