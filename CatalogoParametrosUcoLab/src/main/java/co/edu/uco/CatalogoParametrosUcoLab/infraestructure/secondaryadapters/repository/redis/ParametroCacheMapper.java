package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;

/** Representacion JSON explicita: no requiere cambiar la entidad del nucleo ni usar tipado polimorfico. */
public enum ParametroCacheMapper {
    INSTANCE;

    public ParametroCacheData toData(final ParametroEntity entity) {
        return new ParametroCacheData(entity.getId(), entity.getNombre(), entity.getIdFuncionalidad(),
                entity.getIdTipoParametro(), entity.isActivo());
    }

    public ParametroEntity toEntity(final ParametroCacheData data) {
        return ParametroEntity.create(data.id(), data.nombre(), data.idFuncionalidad(), data.idTipoParametro(),
                data.activo());
    }

    public record ParametroCacheData(UUID id, String nombre, UUID idFuncionalidad, UUID idTipoParametro,
            boolean activo) {
    }
}
