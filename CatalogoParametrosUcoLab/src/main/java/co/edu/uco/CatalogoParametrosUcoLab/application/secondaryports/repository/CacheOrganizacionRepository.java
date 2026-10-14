package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;

/** Puerto de cache independiente de Redis. La version aisla lecturas concurrentes con escrituras. */
public interface CacheOrganizacionRepository {
    String obtenerVersion();

    Optional<List<OrganizacionEntity>> consultar(String version, String clave);

    void guardar(String version, String clave, List<OrganizacionEntity> datos);

    void invalidar();
}
