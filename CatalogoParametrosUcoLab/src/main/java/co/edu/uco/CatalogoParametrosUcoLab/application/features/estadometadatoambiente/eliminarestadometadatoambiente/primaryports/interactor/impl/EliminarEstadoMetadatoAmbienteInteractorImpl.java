package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.eliminarestadometadatoambiente.primaryports.interactor.impl;

import java.util.UUID;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.eliminarestadometadatoambiente.EliminarEstadoMetadatoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadometadatoambiente.eliminarestadometadatoambiente.primaryports.interactor.EliminarEstadoMetadatoAmbienteInteractor;

@Service
public final class EliminarEstadoMetadatoAmbienteInteractorImpl implements EliminarEstadoMetadatoAmbienteInteractor {
    private final EliminarEstadoMetadatoAmbiente useCase;
    public EliminarEstadoMetadatoAmbienteInteractorImpl(final EliminarEstadoMetadatoAmbiente useCase) { this.useCase = useCase; }
    @Override public void execute(final UUID id) { useCase.execute(id); }
}
