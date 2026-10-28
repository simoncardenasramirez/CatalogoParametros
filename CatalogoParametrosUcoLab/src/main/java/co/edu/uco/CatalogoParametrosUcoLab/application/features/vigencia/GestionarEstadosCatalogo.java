package co.edu.uco.CatalogoParametrosUcoLab.application.features.vigencia;

import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.RegistroVigencia;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoRegistroVigencia;

public interface GestionarEstadosCatalogo {
    <T> T guardar(RegistroVigencia registro, boolean nuevo, Supplier<T> persistir);
    void sincronizar();
    void validarRegistro(RegistroVigencia registro, boolean nuevo);
    boolean disponible(TipoRegistroVigencia tipo, UUID id);
    Set<UUID> parametrosDisponibles(UUID aplicacion, UUID ambiente);
}
