package co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.eliminarambiente.primaryports.interactor.impl;

import java.util.UUID;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.eliminarambiente.EliminarAmbiente;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.ambiente.eliminarambiente.primaryports.interactor.EliminarAmbienteInteractor;

@Service
public final class EliminarAmbienteInteractorImpl implements EliminarAmbienteInteractor {
    private final EliminarAmbiente useCase;
    public EliminarAmbienteInteractorImpl(final EliminarAmbiente useCase) { this.useCase = useCase; }
    @Override public void execute(final UUID id) { useCase.execute(id); }
}
