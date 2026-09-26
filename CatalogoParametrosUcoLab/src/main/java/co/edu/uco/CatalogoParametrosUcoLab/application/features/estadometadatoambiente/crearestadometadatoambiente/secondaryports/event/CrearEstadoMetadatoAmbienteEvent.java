package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.crearestadometadatoambiente.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.secondaryports.event.EstadoMetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;

public final class CrearEstadoMetadatoAmbienteEvent implements EstadoMetadatoAmbienteEvent {
    private EstadoMetadatoAmbienteEntity estadometadatoambiente;
    private EventType event;

    public enum EventType { CREATED }

    public CrearEstadoMetadatoAmbienteEvent(final EstadoMetadatoAmbienteEntity estadometadatoambiente, final EventType event) {
        setEstadoMetadatoAmbiente(estadometadatoambiente);
        setEvent(event);
    }

    public static CrearEstadoMetadatoAmbienteEvent created(final EstadoMetadatoAmbienteEntity estadometadatoambiente) {
        return new CrearEstadoMetadatoAmbienteEvent(estadometadatoambiente, EventType.CREATED);
    }

    public EstadoMetadatoAmbienteEntity getEstadoMetadatoAmbiente() { return estadometadatoambiente; }
    public void setEstadoMetadatoAmbiente(final EstadoMetadatoAmbienteEntity value) { estadometadatoambiente = value; }
    public EventType getEvent() { return event; }
    public void setEvent(final EventType value) { event = value; }
}
