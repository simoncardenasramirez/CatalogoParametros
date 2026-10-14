package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;

/** Puerto de cache independiente de Redis. La version aisla lecturas concurrentes con escrituras. */
public interface CacheEstadoMetadatoAmbienteRepository {
    String obtenerVersion();

    Optional<List<EstadoMetadatoAmbienteEntity>> consultar(String version, String clave);

    void guardar(String version, String clave, List<EstadoMetadatoAmbienteEntity> datos);

    void invalidar();
}
