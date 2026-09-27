package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.estadometadatoambiente;

import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoMetadatoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.AbstractNamedSurrealRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;

@Repository
public final class SurrealDbEstadoMetadatoAmbienteRepository
        extends AbstractNamedSurrealRepository<EstadoMetadatoAmbienteEntity>
        implements EstadoMetadatoAmbienteRepository {
    public SurrealDbEstadoMetadatoAmbienteRepository(final SurrealDbClient client) {
        super(client, "estados_metadato_ambiente", EstadoMetadatoAmbienteEntity::create,
                EstadoMetadatoAmbienteEntity::getId, EstadoMetadatoAmbienteEntity::getNombre);
    }
}
