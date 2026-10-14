package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import java.util.UUID;
import java.time.OffsetDateTime;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;

/** Representacion JSON explicita, independiente de la construccion de la entidad. */
public enum FuncionalidadCacheMapper {
    INSTANCE;

    public FuncionalidadCacheData toData(final FuncionalidadEntity entity) {
        return new FuncionalidadCacheData(entity.getId(), entity.getNombre(), entity.getIdModulo(), entity.isActivo(),
                serializarFecha(entity.getFechaInicio()), serializarFecha(entity.getFechaFinal()));
    }

    public FuncionalidadEntity toEntity(final FuncionalidadCacheData data) {
        return FuncionalidadEntity.create(data.id(), data.nombre(), data.idModulo(), data.activo(),
                leerFecha(data.fechaInicio()), leerFecha(data.fechaFinal()));
    }

    private String serializarFecha(final OffsetDateTime fecha) {
        return fecha == null ? null : fecha.toString();
    }

    private OffsetDateTime leerFecha(final String fecha) {
        return fecha == null ? null : OffsetDateTime.parse(fecha);
    }

    public record FuncionalidadCacheData(UUID id, String nombre, UUID idModulo, boolean activo,
            String fechaInicio, String fechaFinal) {
    }
}
