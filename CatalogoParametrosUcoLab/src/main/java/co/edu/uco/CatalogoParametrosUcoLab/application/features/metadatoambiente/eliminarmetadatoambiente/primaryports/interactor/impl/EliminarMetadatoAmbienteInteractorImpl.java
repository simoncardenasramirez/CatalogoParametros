package co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.eliminarmetadatoambiente.primaryports.interactor.impl;

import java.util.UUID;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.eliminarmetadatoambiente.EliminarMetadatoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.metadatoambiente.eliminarmetadatoambiente.primaryports.interactor.EliminarMetadatoAmbienteInteractor;

@Service
public final class EliminarMetadatoAmbienteInteractorImpl implements EliminarMetadatoAmbienteInteractor {
    private final EliminarMetadatoAmbiente useCase;
    public EliminarMetadatoAmbienteInteractorImpl(final EliminarMetadatoAmbiente useCase) { this.useCase = useCase; }
    @Override public void execute(final UUID id) { useCase.execute(id); }
}
