package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.crearmetadatoambiente.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.secondaryports.event.MetadatoAmbienteEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;

public final class CrearMetadatoAmbienteEvent implements MetadatoAmbienteEvent {
    private MetadatoAmbienteEntity metadatoambiente;
    private EventType event;

    public enum EventType { CREATED }

    public CrearMetadatoAmbienteEvent(final MetadatoAmbienteEntity metadatoambiente, final EventType event) {
        setMetadatoAmbiente(metadatoambiente);
        setEvent(event);
    }

    public static CrearMetadatoAmbienteEvent created(final MetadatoAmbienteEntity metadatoambiente) {
        return new CrearMetadatoAmbienteEvent(metadatoambiente, EventType.CREATED);
    }

    public MetadatoAmbienteEntity getMetadatoAmbiente() { return metadatoambiente; }
    public void setMetadatoAmbiente(final MetadatoAmbienteEntity value) { metadatoambiente = value; }
    public EventType getEvent() { return event; }
    public void setEvent(final EventType value) { event = value; }
}
