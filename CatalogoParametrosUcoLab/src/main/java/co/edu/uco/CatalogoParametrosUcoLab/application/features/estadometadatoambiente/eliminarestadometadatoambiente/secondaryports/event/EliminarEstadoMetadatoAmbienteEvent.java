package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.eliminarestadometadatoambiente.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.secondaryports.event.EstadoMetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;

public final class EliminarEstadoMetadatoAmbienteEvent implements EstadoMetadatoAmbienteEvent {
    private EstadoMetadatoAmbienteEntity estadometadatoambiente;
    private EventType event;

    public enum EventType { DELETED }

    public EliminarEstadoMetadatoAmbienteEvent(final EstadoMetadatoAmbienteEntity estadometadatoambiente, final EventType event) {
        setEstadoMetadatoAmbiente(estadometadatoambiente);
        setEvent(event);
    }

    public static EliminarEstadoMetadatoAmbienteEvent deleted(final EstadoMetadatoAmbienteEntity estadometadatoambiente) {
        return new EliminarEstadoMetadatoAmbienteEvent(estadometadatoambiente, EventType.DELETED);
    }

    public EstadoMetadatoAmbienteEntity getEstadoMetadatoAmbiente() { return estadometadatoambiente; }
    public void setEstadoMetadatoAmbiente(final EstadoMetadatoAmbienteEntity value) { estadometadatoambiente = value; }
    public EventType getEvent() { return event; }
    public void setEvent(final EventType value) { event = value; }
}
