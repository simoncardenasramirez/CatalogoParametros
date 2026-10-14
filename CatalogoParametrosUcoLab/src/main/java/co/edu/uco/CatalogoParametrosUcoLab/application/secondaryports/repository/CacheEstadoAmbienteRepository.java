package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoAmbienteEntity;

/** Puerto de cache independiente de Redis. La version aisla lecturas concurrentes con escrituras. */
public interface CacheEstadoAmbienteRepository {
    String obtenerVersion();

    Optional<List<EstadoAmbienteEntity>> consultar(String version, String clave);

    void guardar(String version, String clave, List<EstadoAmbienteEntity> datos);

    void invalidar();
}
