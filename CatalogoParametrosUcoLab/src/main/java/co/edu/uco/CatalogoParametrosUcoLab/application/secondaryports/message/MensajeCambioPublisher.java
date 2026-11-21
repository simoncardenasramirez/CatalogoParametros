package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.message;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.publisher.Publisher;

/** Puerto de salida para distribuir avisos de MessageUcoLab dentro de la aplicación. */
public interface MensajeCambioPublisher extends Publisher<MensajeCambio> {
}
