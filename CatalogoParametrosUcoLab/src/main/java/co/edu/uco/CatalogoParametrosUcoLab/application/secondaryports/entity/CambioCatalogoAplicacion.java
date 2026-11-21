package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;
import java.util.UUID;

import tools.jackson.databind.JsonNode;

/** Notificacion de cambio; puede incluir el valor nuevo del recurso cuando aplica. */
public record CambioCatalogoAplicacion(String recurso, String operacion, UUID id, UUID idPadre,
        UUID idAmbiente, String nombreRecurso, JsonNode valorNuevo) {

    /** Compatibilidad con consumidores que no necesitan acotar el cambio a un ambiente. */
    public CambioCatalogoAplicacion(final String recurso, final String operacion, final UUID id,
            final UUID idPadre) {
        this(recurso, operacion, id, idPadre, null, null, null);
    }

    public CambioCatalogoAplicacion(final String recurso, final String operacion, final UUID id,
            final UUID idPadre, final UUID idAmbiente) {
        this(recurso, operacion, id, idPadre, idAmbiente, null, null);
    }

    public CambioCatalogoAplicacion(final String recurso, final String operacion, final UUID id,
            final UUID idPadre, final UUID idAmbiente, final String nombreRecurso) {
        this(recurso, operacion, id, idPadre, idAmbiente, nombreRecurso, null);
    }

    public CambioCatalogoAplicacion(final String recurso, final String operacion, final UUID id,
            final UUID idPadre, final UUID idAmbiente, final String nombreRecurso, final JsonNode valorNuevo) {
        this.recurso = recurso;
        this.operacion = operacion;
        this.id = id;
        this.idPadre = idPadre;
        this.idAmbiente = idAmbiente;
        this.nombreRecurso = nombreRecurso;
        this.valorNuevo = valorNuevo == null ? null : valorNuevo.deepCopy();
    }
}
