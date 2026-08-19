package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoParametroEntity;

public interface TipoParametroRepository {

    TipoParametroEntity save(TipoParametroEntity tipoParametro);

    boolean existsByNombre(String nombre);

    Optional<TipoParametroEntity> findById(UUID id);

    List<TipoParametroEntity> findAll();

    TipoParametroEntity update(TipoParametroEntity tipoParametro);

    void deleteById(UUID id);
}
