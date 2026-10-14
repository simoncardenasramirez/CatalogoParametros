package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoParametroEntity;

/** Puerto de cache independiente de Redis. La version aisla lecturas concurrentes con escrituras. */
public interface CacheTipoParametroRepository {
    String obtenerVersion();

    Optional<List<TipoParametroEntity>> consultar(String version, String clave);

    void guardar(String version, String clave, List<TipoParametroEntity> datos);

    void invalidar();
}
