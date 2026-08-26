package co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.eliminarmodulo.secondaryports.event;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.modulo.secondaryports.event.ModuloEvent;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
public final class EliminarModuloEvent implements ModuloEvent {
    private ModuloEntity modulo;
    private EventType event;
    public enum EventType { DELETED }
    private EliminarModuloEvent(final ModuloEntity modulo, final EventType event) { this.modulo = modulo; this.event = event; }
    public static EliminarModuloEvent deleted(final ModuloEntity modulo) { return new EliminarModuloEvent(modulo, EventType.DELETED); }
    public ModuloEntity getModulo() { return modulo; }
    public void setModulo(final ModuloEntity modulo) { this.modulo = modulo; }
    public EventType getEvent() { return event; }
    public void setEvent(final EventType event) { this.event = event; }
}
