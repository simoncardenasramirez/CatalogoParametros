package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.primaryports.interactor;

import java.util.List;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.crearmetadatostodosambientes.primaryports.dto.CrearMetadatosTodosAmbientesDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.InteractorWithReturn;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;

public interface CrearMetadatosTodosAmbientesInteractor extends InteractorWithReturn<CrearMetadatosTodosAmbientesDtoRequest, List<MetadatoEntity>> { }
