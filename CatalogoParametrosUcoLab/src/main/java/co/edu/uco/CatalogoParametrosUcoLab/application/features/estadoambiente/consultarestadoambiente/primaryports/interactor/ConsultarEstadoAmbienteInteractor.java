package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.consultarestadoambiente.primaryports.interactor;

import java.util.List;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoAmbienteEntity;

public interface ConsultarEstadoAmbienteInteractor {
    EstadoAmbienteEntity execute(UUID id);
    List<EstadoAmbienteEntity> execute(int pagina, int tamanoPagina);
}
