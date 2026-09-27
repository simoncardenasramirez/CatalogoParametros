package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.eliminarmetadatoambiente.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.secondaryports.event.MetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;

public final class EliminarMetadatoAmbienteEvent implements MetadatoAmbienteEvent {
    private MetadatoAmbienteEntity metadatoambiente;
    private EventType event;

    public enum EventType { DELETED }

    public EliminarMetadatoAmbienteEvent(final MetadatoAmbienteEntity metadatoambiente, final EventType event) {
        setMetadatoAmbiente(metadatoambiente);
        setEvent(event);
    }

    public static EliminarMetadatoAmbienteEvent deleted(final MetadatoAmbienteEntity metadatoambiente) {
        return new EliminarMetadatoAmbienteEvent(metadatoambiente, EventType.DELETED);
    }

    public MetadatoAmbienteEntity getMetadatoAmbiente() { return metadatoambiente; }
    public void setMetadatoAmbiente(final MetadatoAmbienteEntity value) { metadatoambiente = value; }
    public EventType getEvent() { return event; }
    public void setEvent(final EventType value) { event = value; }
}
