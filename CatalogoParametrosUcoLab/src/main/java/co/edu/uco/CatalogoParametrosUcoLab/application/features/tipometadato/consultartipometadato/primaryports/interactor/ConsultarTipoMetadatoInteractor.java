package co.edu.uco.CatalogoParametrosUcoLab.application.features.tipometadato.consultartipometadato.primaryports.interactor;

import java.util.List;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoMetadatoEntity;

public interface ConsultarTipoMetadatoInteractor {
    List<TipoMetadatoEntity> execute();
    List<TipoMetadatoEntity> execute(UUID id);
}
