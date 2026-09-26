package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NamedCatalogRepository<T> {
    T save(T entity);
    T update(T entity);
    void deleteById(UUID id);
    boolean existsByNombre(String nombre);
    Optional<T> findById(UUID id);
    List<T> findAll();
    List<T> findAllPaginado(int pagina, int tamanoPagina);
}
