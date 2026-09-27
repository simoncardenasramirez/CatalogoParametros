package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.ambiente;

import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.AmbienteRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.AbstractNamedSurrealRepository;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;

@Repository
public final class SurrealDbAmbienteRepository extends AbstractNamedSurrealRepository<AmbienteEntity>
        implements AmbienteRepository {
    public SurrealDbAmbienteRepository(final SurrealDbClient client) {
        super(client, "ambientes", AmbienteEntity::create, AmbienteEntity::getId, AmbienteEntity::getNombre);
    }
}
