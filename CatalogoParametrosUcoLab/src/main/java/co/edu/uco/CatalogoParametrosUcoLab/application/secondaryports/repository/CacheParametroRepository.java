package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;

/** Puerto de cache independiente de Redis. La version aisla lecturas concurrentes con escrituras. */
public interface CacheParametroRepository {
    String obtenerVersion();

    Optional<List<ParametroEntity>> consultar(String version, String clave);

    void guardar(String version, String clave, List<ParametroEntity> parametros);

    void invalidar();
}
