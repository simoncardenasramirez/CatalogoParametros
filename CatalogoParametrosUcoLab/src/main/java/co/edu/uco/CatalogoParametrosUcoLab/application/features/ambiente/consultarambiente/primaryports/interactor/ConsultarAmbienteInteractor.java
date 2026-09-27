package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.consultarambiente.primaryports.interactor;

import java.util.List;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;

public interface ConsultarAmbienteInteractor {
    AmbienteEntity execute(UUID id);
    List<AmbienteEntity> execute(int pagina, int tamanoPagina);
}
