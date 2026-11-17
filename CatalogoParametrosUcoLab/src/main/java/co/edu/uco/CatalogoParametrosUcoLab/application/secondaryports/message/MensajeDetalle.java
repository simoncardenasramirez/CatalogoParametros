package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message;

public record MensajeDetalle(
        String code,
        String title,
        String content,
        String type,
        String category,
        String functionality) {
}
