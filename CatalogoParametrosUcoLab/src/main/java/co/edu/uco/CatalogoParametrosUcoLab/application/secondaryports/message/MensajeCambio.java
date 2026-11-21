package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message;

/** Aviso sin contenido del mensaje; el consumidor vuelve a consultarlo por código. */
public record MensajeCambio(String eventType, String messageCode, String environmentId, String eventId) {
}
