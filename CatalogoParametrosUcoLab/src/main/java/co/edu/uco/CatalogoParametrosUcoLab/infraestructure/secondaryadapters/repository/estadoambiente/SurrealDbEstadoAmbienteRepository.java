package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.estadoambiente;

import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoAmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.AbstractNamedSurrealRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;

@Repository
public final class SurrealDbEstadoAmbienteRepository extends AbstractNamedSurrealRepository<EstadoAmbienteEntity>
        implements EstadoAmbienteRepository {
    public SurrealDbEstadoAmbienteRepository(final SurrealDbClient client) {
        super(client, "estados_ambiente", EstadoAmbienteEntity::create, EstadoAmbienteEntity::getId,
                EstadoAmbienteEntity::getNombre);
    }
}
