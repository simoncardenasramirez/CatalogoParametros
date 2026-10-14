package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;

/** Puerto de cache independiente de Redis. La version aisla lecturas concurrentes con escrituras. */
public interface CacheFuncionalidadRepository {
    String obtenerVersion();

    Optional<List<FuncionalidadEntity>> consultar(String version, String clave);

    void guardar(String version, String clave, List<FuncionalidadEntity> datos);

    void invalidar();
}
