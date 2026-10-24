package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.publisher;
import reactor.core.publisher.Flux;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.CambioCatalogoAplicacion;
public interface CambiosCatalogoAplicacionPublisher { Flux<CambioCatalogoAplicacion> getStream(); }
