package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.eliminarestadoambiente.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.secondaryports.event.EstadoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoAmbienteEntity;

public final class EliminarEstadoAmbienteEvent implements EstadoAmbienteEvent {
    private EstadoAmbienteEntity estadoambiente;
    private EventType event;

    public enum EventType { DELETED }

    public EliminarEstadoAmbienteEvent(final EstadoAmbienteEntity estadoambiente, final EventType event) {
        setEstadoAmbiente(estadoambiente);
        setEvent(event);
    }

    public static EliminarEstadoAmbienteEvent deleted(final EstadoAmbienteEntity estadoambiente) {
        return new EliminarEstadoAmbienteEvent(estadoambiente, EventType.DELETED);
    }

    public EstadoAmbienteEntity getEstadoAmbiente() { return estadoambiente; }
    public void setEstadoAmbiente(final EstadoAmbienteEntity value) { estadoambiente = value; }
    public EventType getEvent() { return event; }
    public void setEvent(final EventType value) { event = value; }
}
