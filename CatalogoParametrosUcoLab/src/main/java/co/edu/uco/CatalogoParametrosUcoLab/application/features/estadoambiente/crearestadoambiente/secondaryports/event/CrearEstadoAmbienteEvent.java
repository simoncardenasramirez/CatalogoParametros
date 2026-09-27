package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.crearestadoambiente.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.secondaryports.event.EstadoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoAmbienteEntity;

public final class CrearEstadoAmbienteEvent implements EstadoAmbienteEvent {
    private EstadoAmbienteEntity estadoambiente;
    private EventType event;

    public enum EventType { CREATED }

    public CrearEstadoAmbienteEvent(final EstadoAmbienteEntity estadoambiente, final EventType event) {
        setEstadoAmbiente(estadoambiente);
        setEvent(event);
    }

    public static CrearEstadoAmbienteEvent created(final EstadoAmbienteEntity estadoambiente) {
        return new CrearEstadoAmbienteEvent(estadoambiente, EventType.CREATED);
    }

    public EstadoAmbienteEntity getEstadoAmbiente() { return estadoambiente; }
    public void setEstadoAmbiente(final EstadoAmbienteEntity value) { estadoambiente = value; }
    public EventType getEvent() { return event; }
    public void setEvent(final EventType value) { event = value; }
}
