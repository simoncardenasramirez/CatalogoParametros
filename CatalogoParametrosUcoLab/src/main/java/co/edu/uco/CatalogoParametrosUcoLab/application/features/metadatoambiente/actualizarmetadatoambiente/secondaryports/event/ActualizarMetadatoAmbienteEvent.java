package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.actualizarmetadatoambiente.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.secondaryports.event.MetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;

public final class ActualizarMetadatoAmbienteEvent implements MetadatoAmbienteEvent {
    private MetadatoAmbienteEntity metadatoambiente;
    private EventType event;

    public enum EventType { UPDATED }

    public ActualizarMetadatoAmbienteEvent(final MetadatoAmbienteEntity metadatoambiente, final EventType event) {
        setMetadatoAmbiente(metadatoambiente);
        setEvent(event);
    }

    public static ActualizarMetadatoAmbienteEvent updated(final MetadatoAmbienteEntity metadatoambiente) {
        return new ActualizarMetadatoAmbienteEvent(metadatoambiente, EventType.UPDATED);
    }

    public MetadatoAmbienteEntity getMetadatoAmbiente() { return metadatoambiente; }
    public void setMetadatoAmbiente(final MetadatoAmbienteEntity value) { metadatoambiente = value; }
    public EventType getEvent() { return event; }
    public void setEvent(final EventType value) { event = value; }
}
