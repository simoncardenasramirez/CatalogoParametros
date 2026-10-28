package co.edu.uco.CatalogoParametrosUcoLab.application.features.vigencia.primaryports.interactor;

import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Service;
import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.ControlEstadoCatalogo;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.vigencia.GestionarEstadosCatalogo;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.*;

@Service
public final class ControlEstadoCatalogoInteractorImpl implements ControlEstadoCatalogo {
    private final GestionarEstadosCatalogo casoDeUso;
    public ControlEstadoCatalogoInteractorImpl(final GestionarEstadosCatalogo casoDeUso) {
        this.casoDeUso = casoDeUso;
    }
    @Override public void sincronizar() { casoDeUso.sincronizar(); }
    @Override public <T> T guardar(final RegistroVigencia registro, final boolean nuevo,
            final java.util.function.Supplier<T> persistir) {
        return casoDeUso.guardar(registro, nuevo, persistir);
    }
    @Override public void validarRegistro(final RegistroVigencia registro, final boolean nuevo) {
        casoDeUso.validarRegistro(registro, nuevo);
    }
    @Override public boolean disponible(final TipoRegistroVigencia tipo, final UUID id) {
        return casoDeUso.disponible(tipo, id);
    }
    @Override public Set<UUID> parametrosDisponibles(final UUID aplicacion, final UUID ambiente) {
        return casoDeUso.parametrosDisponibles(aplicacion, ambiente);
    }
}
