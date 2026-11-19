package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.mensaje;

public record MensajeResponse(
        String code,
        String title,
        String content,
        String type,
        String category,
        String functionality) {
}
