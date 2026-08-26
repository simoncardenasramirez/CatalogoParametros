package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadato.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.secondaryports.event.MetadatoEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;

public final class CrearMetadatoEvent implements MetadatoEvent {
    private final MetadatoEntity metadato;
    private final EventType event;
    public enum EventType { CREATED }
    private CrearMetadatoEvent(final MetadatoEntity metadato) { this.metadato = metadato; this.event = EventType.CREATED; }
    public static CrearMetadatoEvent created(final MetadatoEntity metadato) { return new CrearMetadatoEvent(metadato); }
    public MetadatoEntity getMetadato() { return metadato; }
    public EventType getEvent() { return event; }
}
