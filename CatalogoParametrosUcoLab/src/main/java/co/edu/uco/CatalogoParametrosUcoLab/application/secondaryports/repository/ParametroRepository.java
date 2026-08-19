package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;

public interface ParametroRepository {

    ParametroEntity save(ParametroEntity parametro);

    ParametroEntity update(ParametroEntity parametro);

    void deleteById(UUID id);

    boolean existsByNombre(String nombre);

    Optional<ParametroEntity> findById(UUID id);

    List<ParametroEntity> findAll();

    List<ParametroEntity> findAllPaginado(int pagina, int tamanoPagina);

    List<ParametroEntity> findByIdFuncionalidad(UUID idFuncionalidad);
}
