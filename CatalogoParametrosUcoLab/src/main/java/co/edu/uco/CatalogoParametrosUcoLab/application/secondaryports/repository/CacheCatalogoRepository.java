package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;

/** Contrato de cache versionada independiente del adaptador de almacenamiento. */
public interface CacheCatalogoRepository<E> {
    String obtenerVersion();

    Optional<List<E>> consultar(String version, String clave);

    void guardar(String version, String clave, List<E> datos);

    void invalidar();
}
