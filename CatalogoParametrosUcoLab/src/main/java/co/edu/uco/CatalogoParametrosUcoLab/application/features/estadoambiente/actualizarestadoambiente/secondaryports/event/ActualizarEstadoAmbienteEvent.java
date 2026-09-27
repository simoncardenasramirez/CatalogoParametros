package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.actualizarestadoambiente.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.secondaryports.event.EstadoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoAmbienteEntity;

public final class ActualizarEstadoAmbienteEvent implements EstadoAmbienteEvent {
    private EstadoAmbienteEntity estadoambiente;
    private EventType event;

    public enum EventType { UPDATED }

    public ActualizarEstadoAmbienteEvent(final EstadoAmbienteEntity estadoambiente, final EventType event) {
        setEstadoAmbiente(estadoambiente);
        setEvent(event);
    }

    public static ActualizarEstadoAmbienteEvent updated(final EstadoAmbienteEntity estadoambiente) {
        return new ActualizarEstadoAmbienteEvent(estadoambiente, EventType.UPDATED);
    }

    public EstadoAmbienteEntity getEstadoAmbiente() { return estadoambiente; }
    public void setEstadoAmbiente(final EstadoAmbienteEntity value) { estadoambiente = value; }
    public EventType getEvent() { return event; }
    public void setEvent(final EventType value) { event = value; }
}
