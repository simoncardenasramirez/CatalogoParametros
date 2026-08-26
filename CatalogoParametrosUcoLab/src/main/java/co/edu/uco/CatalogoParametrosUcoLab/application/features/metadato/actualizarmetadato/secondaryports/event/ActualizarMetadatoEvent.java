package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.secondaryports.event;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.secondaryports.event.MetadatoEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;
public final class ActualizarMetadatoEvent implements MetadatoEvent {
    private final MetadatoEntity metadato; private final EventType event = EventType.UPDATED;
    public enum EventType { UPDATED }
    private ActualizarMetadatoEvent(final MetadatoEntity metadato) { this.metadato = metadato; }
    public static ActualizarMetadatoEvent updated(final MetadatoEntity value) { return new ActualizarMetadatoEvent(value); }
    public MetadatoEntity getMetadato() { return metadato; } public EventType getEvent() { return event; }
}
