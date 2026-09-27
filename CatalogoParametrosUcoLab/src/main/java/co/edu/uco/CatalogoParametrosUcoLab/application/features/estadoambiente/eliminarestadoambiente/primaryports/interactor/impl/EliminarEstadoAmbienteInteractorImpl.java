package co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.eliminarestadoambiente.primaryports.interactor.impl;

import java.util.UUID;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.eliminarestadoambiente.EliminarEstadoAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.estadoambiente.eliminarestadoambiente.primaryports.interactor.EliminarEstadoAmbienteInteractor;

@Service
public final class EliminarEstadoAmbienteInteractorImpl implements EliminarEstadoAmbienteInteractor {
    private final EliminarEstadoAmbiente useCase;
    public EliminarEstadoAmbienteInteractorImpl(final EliminarEstadoAmbiente useCase) { this.useCase = useCase; }
    @Override public void execute(final UUID id) { useCase.execute(id); }
}
