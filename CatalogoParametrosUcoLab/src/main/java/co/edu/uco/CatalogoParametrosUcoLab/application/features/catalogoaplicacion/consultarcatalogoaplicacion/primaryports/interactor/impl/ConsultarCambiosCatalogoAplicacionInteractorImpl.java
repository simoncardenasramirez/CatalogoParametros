package co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.consultarcatalogoaplicacion.primaryports.interactor.impl;
import java.util.UUID;
import reactor.core.publisher.Flux;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.consultarcatalogoaplicacion.ConsultarCambiosCatalogoAplicacion;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.consultarcatalogoaplicacion.primaryports.interactor.ConsultarCambiosCatalogoAplicacionInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.CambioCatalogoAplicacion;
@Service
public final class ConsultarCambiosCatalogoAplicacionInteractorImpl implements ConsultarCambiosCatalogoAplicacionInteractor {
    private final ConsultarCambiosCatalogoAplicacion casoUso;
    public ConsultarCambiosCatalogoAplicacionInteractorImpl(ConsultarCambiosCatalogoAplicacion casoUso) { this.casoUso=casoUso; }
    @Override public Flux<CambioCatalogoAplicacion> execute(UUID aplicacion) { return casoUso.execute(aplicacion); }
}
