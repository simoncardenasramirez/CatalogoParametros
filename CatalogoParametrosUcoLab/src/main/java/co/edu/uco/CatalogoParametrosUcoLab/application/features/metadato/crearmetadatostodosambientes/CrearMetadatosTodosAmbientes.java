package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes;

import java.util.List;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.usecase.domain.CrearMetadatosTodosAmbientesDomain;

public interface CrearMetadatosTodosAmbientes {
    List<MetadatoEntity> execute(CrearMetadatosTodosAmbientesDomain data);
}
