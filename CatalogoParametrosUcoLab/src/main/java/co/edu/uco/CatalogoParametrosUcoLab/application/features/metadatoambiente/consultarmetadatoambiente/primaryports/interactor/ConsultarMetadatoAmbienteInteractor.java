package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.consultarmetadatoambiente.primaryports.interactor;

import java.util.List;
import java.util.UUID;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;

public interface ConsultarMetadatoAmbienteInteractor {
    MetadatoAmbienteEntity execute(UUID id);
    List<MetadatoAmbienteEntity> execute(int pagina, int tamanoPagina);
}
