package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.redis;

import java.util.UUID;
import java.time.OffsetDateTime;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.OrganizacionEntity;

/** Representacion JSON explicita, independiente de la construccion de la entidad. */
public enum OrganizacionCacheMapper {
    INSTANCE;

    public OrganizacionCacheData toData(final OrganizacionEntity entity) {
        return new OrganizacionCacheData(entity.getId(), entity.getNombre(),
                serializarFecha(entity.getFechaInicio()), serializarFecha(entity.getFechaFinal()));
    }

    public OrganizacionEntity toEntity(final OrganizacionCacheData data) {
        return OrganizacionEntity.create(data.id(), data.nombre(),
                leerFecha(data.fechaInicio()), leerFecha(data.fechaFinal()));
    }

    private String serializarFecha(final OffsetDateTime fecha) {
        return fecha == null ? null : fecha.toString();
    }

    private OffsetDateTime leerFecha(final String fecha) {
        return fecha == null ? null : OffsetDateTime.parse(fecha);
    }

    public record OrganizacionCacheData(UUID id, String nombre,
            String fechaInicio, String fechaFinal) {
    }
}
