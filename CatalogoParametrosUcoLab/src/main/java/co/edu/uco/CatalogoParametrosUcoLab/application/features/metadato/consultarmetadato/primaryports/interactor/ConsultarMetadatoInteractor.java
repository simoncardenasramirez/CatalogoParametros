package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.consultarmetadato.primaryports.interactor;

import java.util.List;
import java.util.UUID;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;

public interface ConsultarMetadatoInteractor {
    List<MetadatoEntity> execute();
    List<MetadatoEntity> execute(UUID id);
    List<MetadatoEntity> executeByParametro(UUID idParametro);
}
