package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;
import java.util.UUID;
/** Notificacion sin valores ni credenciales; el cliente vuelve a consultar su alcance. */
public record CambioCatalogoAplicacion(String recurso, String operacion, UUID id, UUID idPadre) { }
