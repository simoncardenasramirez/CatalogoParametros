package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.consultarestadometadatoambiente.primaryports.interactor;

import java.util.List;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;

public interface ConsultarEstadoMetadatoAmbienteInteractor {
    EstadoMetadatoAmbienteEntity execute(UUID id);
    List<EstadoMetadatoAmbienteEntity> execute(int pagina, int tamanoPagina);
}
