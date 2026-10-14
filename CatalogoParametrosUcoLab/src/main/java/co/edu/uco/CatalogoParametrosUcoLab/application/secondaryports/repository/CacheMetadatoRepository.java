package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;

/** Puerto de cache independiente de Redis. La version aisla lecturas concurrentes con escrituras. */
public interface CacheMetadatoRepository {
    String obtenerVersion();

    Optional<List<MetadatoEntity>> consultar(String version, String clave);

    void guardar(String version, String clave, List<MetadatoEntity> datos);

    void invalidar();
}
