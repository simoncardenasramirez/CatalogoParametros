package co.edu.uco.CatalogoParametrosUcoLab.application.primaryports;

import java.util.Set;
import java.util.UUID;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.RegistroVigencia;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoRegistroVigencia;

public interface ControlEstadoCatalogo {
    void sincronizar();
    void validarRegistro(RegistroVigencia registro, boolean nuevo);
    boolean disponible(TipoRegistroVigencia tipo, UUID id);
    Set<UUID> parametrosDisponibles(UUID aplicacion, UUID ambiente);
    <T> T guardar(RegistroVigencia registro, boolean nuevo, java.util.function.Supplier<T> persistir);
}
