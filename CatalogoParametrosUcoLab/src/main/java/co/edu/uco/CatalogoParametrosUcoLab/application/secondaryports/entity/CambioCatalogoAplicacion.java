package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;
import java.util.UUID;
/** Notificacion sin valores ni credenciales; el cliente vuelve a consultar su alcance. */
public record CambioCatalogoAplicacion(String recurso, String operacion, UUID id, UUID idPadre,
        UUID idAmbiente) {

    /** Compatibilidad con consumidores que no necesitan acotar el cambio a un ambiente. */
    public CambioCatalogoAplicacion(final String recurso, final String operacion, final UUID id,
            final UUID idPadre) {
        this(recurso, operacion, id, idPadre, null);
    }
}
