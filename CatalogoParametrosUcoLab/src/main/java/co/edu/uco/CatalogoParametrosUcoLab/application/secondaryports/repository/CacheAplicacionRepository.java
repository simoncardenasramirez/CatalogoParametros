package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;

/** Puerto de cache independiente de Redis. La version aisla lecturas concurrentes con escrituras. */
public interface CacheAplicacionRepository {
    String obtenerVersion();

    Optional<List<AplicacionEntity>> consultar(String version, String clave);

    void guardar(String version, String clave, List<AplicacionEntity> datos);

    void invalidar();
}
