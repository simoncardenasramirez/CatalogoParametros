package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoMetadatoEntity;

/** Puerto de cache independiente de Redis. La version aisla lecturas concurrentes con escrituras. */
public interface CacheTipoMetadatoRepository {
    String obtenerVersion();

    Optional<List<TipoMetadatoEntity>> consultar(String version, String clave);

    void guardar(String version, String clave, List<TipoMetadatoEntity> datos);

    void invalidar();
}
