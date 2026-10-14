package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import java.util.UUID;
import java.time.OffsetDateTime;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;

/** Representacion JSON explicita, independiente de la construccion de la entidad. */
public enum ModuloCacheMapper {
    INSTANCE;

    public ModuloCacheData toData(final ModuloEntity entity) {
        return new ModuloCacheData(entity.getId(), entity.getNombre(), entity.getIdAplicacion(), entity.isActivo(),
                serializarFecha(entity.getFechaInicio()), serializarFecha(entity.getFechaFinal()));
    }

    public ModuloEntity toEntity(final ModuloCacheData data) {
        return ModuloEntity.create(data.id(), data.nombre(), data.idAplicacion(), data.activo(),
                leerFecha(data.fechaInicio()), leerFecha(data.fechaFinal()));
    }

    private String serializarFecha(final OffsetDateTime fecha) {
        return fecha == null ? null : fecha.toString();
    }

    private OffsetDateTime leerFecha(final String fecha) {
        return fecha == null ? null : OffsetDateTime.parse(fecha);
    }

    public record ModuloCacheData(UUID id, String nombre, UUID idAplicacion, boolean activo,
            String fechaInicio, String fechaFinal) {
    }
}
