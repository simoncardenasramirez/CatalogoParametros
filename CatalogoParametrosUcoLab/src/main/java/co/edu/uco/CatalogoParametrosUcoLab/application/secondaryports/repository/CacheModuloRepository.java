package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;

/** Puerto de cache independiente de Redis. La version aisla lecturas concurrentes con escrituras. */
public interface CacheModuloRepository {
    String obtenerVersion();

    Optional<List<ModuloEntity>> consultar(String version, String clave);

    void guardar(String version, String clave, List<ModuloEntity> datos);

    void invalidar();
}
