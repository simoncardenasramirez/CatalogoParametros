package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.eliminarmetadato.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.secondaryports.event.MetadatoEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;
public final class EliminarMetadatoEvent implements MetadatoEvent {
    private final MetadatoEntity metadato;
    private final EventType event = EventType.DELETED;
    public enum EventType { DELETED }
    private EliminarMetadatoEvent(final MetadatoEntity entity) { metadato = entity; }
    public static EliminarMetadatoEvent deleted(final MetadatoEntity entity) { return new EliminarMetadatoEvent(entity); }
    public MetadatoEntity getMetadato() { return metadato; }
    public EventType getEvent() { return event; }
}
