package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.publisher;

import java.util.List;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.RegistroVigencia;

public interface CambiosEstadoCatalogoPublisher {
    void publicar(List<RegistroVigencia> registros);
}
