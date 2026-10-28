package co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.consultarcatalogoaplicacion.primaryports.interactor;
import java.util.UUID;
import reactor.core.publisher.Flux;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.CambioCatalogoAplicacion;
public interface ConsultarCambiosCatalogoAplicacionInteractor { Flux<CambioCatalogoAplicacion> execute(UUID aplicacion); }
