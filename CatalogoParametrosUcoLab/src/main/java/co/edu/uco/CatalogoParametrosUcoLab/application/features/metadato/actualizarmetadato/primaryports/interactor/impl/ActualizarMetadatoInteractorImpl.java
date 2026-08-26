package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.primaryports.interactor.impl;
import java.util.UUID;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.ActualizarMetadato;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.primaryports.dto.ActualizarMetadatoDtoRequest;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.primaryports.interactor.ActualizarMetadatoInteractor;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.actualizarmetadato.primaryports.interactor.mapper.ActualizarMetadatoDtoMapper;
@Service
public final class ActualizarMetadatoInteractorImpl implements ActualizarMetadatoInteractor {
    private final ActualizarMetadato useCase;
    public ActualizarMetadatoInteractorImpl(final ActualizarMetadato useCase) { this.useCase = useCase; }
    @Override public void execute(final UUID id, final ActualizarMetadatoDtoRequest data) {
        var input = ActualizarMetadatoDtoMapper.INSTANCE.toDtoInput(data);
        useCase.execute(ActualizarMetadatoDtoMapper.INSTANCE.toDomain(id, input));
    }
}
