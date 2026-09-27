package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.actualizarestadometadatoambiente.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.secondaryports.event.EstadoMetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;

public final class ActualizarEstadoMetadatoAmbienteEvent implements EstadoMetadatoAmbienteEvent {
    private EstadoMetadatoAmbienteEntity estadometadatoambiente;
    private EventType event;

    public enum EventType { UPDATED }

    public ActualizarEstadoMetadatoAmbienteEvent(final EstadoMetadatoAmbienteEntity estadometadatoambiente, final EventType event) {
        setEstadoMetadatoAmbiente(estadometadatoambiente);
        setEvent(event);
    }

    public static ActualizarEstadoMetadatoAmbienteEvent updated(final EstadoMetadatoAmbienteEntity estadometadatoambiente) {
        return new ActualizarEstadoMetadatoAmbienteEvent(estadometadatoambiente, EventType.UPDATED);
    }

    public EstadoMetadatoAmbienteEntity getEstadoMetadatoAmbiente() { return estadometadatoambiente; }
    public void setEstadoMetadatoAmbiente(final EstadoMetadatoAmbienteEntity value) { estadometadatoambiente = value; }
    public EventType getEvent() { return event; }
    public void setEvent(final EventType value) { event = value; }
}
