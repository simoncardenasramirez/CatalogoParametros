package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import java.util.UUID;
import java.time.OffsetDateTime;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;

/** Representacion JSON explicita, independiente de la construccion de la entidad. */
public enum AplicacionCacheMapper {
    INSTANCE;

    public AplicacionCacheData toData(final AplicacionEntity entity) {
        return new AplicacionCacheData(entity.getId(), entity.getNombre(), entity.getIdOrganizacion(), entity.isActiva(),
                serializarFecha(entity.getFechaInicio()), serializarFecha(entity.getFechaFinal()));
    }

    public AplicacionEntity toEntity(final AplicacionCacheData data) {
        return AplicacionEntity.create(data.id(), data.nombre(), data.idOrganizacion(), data.activa(),
                leerFecha(data.fechaInicio()), leerFecha(data.fechaFinal()));
    }

    private String serializarFecha(final OffsetDateTime fecha) {
        return fecha == null ? null : fecha.toString();
    }

    private OffsetDateTime leerFecha(final String fecha) {
        return fecha == null ? null : OffsetDateTime.parse(fecha);
    }

    public record AplicacionCacheData(UUID id, String nombre, UUID idOrganizacion, boolean activa,
            String fechaInicio, String fechaFinal) {
    }
}
