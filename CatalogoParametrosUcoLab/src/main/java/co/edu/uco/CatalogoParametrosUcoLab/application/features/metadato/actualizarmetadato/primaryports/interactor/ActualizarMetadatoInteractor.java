package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.primaryports.interactor;
import java.util.UUID;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.primaryports.dto.ActualizarMetadatoDtoRequest;
public interface ActualizarMetadatoInteractor { void execute(UUID id, ActualizarMetadatoDtoRequest data); }
