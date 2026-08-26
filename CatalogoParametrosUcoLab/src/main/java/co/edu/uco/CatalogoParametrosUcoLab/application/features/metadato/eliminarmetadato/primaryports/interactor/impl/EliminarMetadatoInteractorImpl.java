package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.eliminarmetadato.primaryports.interactor.impl;

import java.util.UUID;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.eliminarmetadato.EliminarMetadato;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadato.eliminarmetadato.primaryports.interactor.EliminarMetadatoInteractor;

@Service
public final class EliminarMetadatoInteractorImpl implements EliminarMetadatoInteractor {
    private final EliminarMetadato useCase;
    public EliminarMetadatoInteractorImpl(final EliminarMetadato useCase) { this.useCase = useCase; }
    @Override public void execute(final UUID id) { useCase.execute(id); }
}
