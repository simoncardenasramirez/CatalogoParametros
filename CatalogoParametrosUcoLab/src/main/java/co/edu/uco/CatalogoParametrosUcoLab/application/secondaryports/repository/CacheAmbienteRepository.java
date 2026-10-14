package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;

/** Puerto de cache independiente de Redis. La version aisla lecturas concurrentes con escrituras. */
public interface CacheAmbienteRepository {
    String obtenerVersion();

    Optional<List<AmbienteEntity>> consultar(String version, String clave);

    void guardar(String version, String clave, List<AmbienteEntity> datos);

    void invalidar();
}
