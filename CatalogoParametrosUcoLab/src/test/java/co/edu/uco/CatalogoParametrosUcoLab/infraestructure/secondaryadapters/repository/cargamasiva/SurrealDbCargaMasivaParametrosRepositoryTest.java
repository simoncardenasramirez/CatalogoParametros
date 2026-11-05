package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.cargamasiva;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain.CargaMasivaLote;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;
import tools.jackson.databind.ObjectMapper;

class SurrealDbCargaMasivaParametrosRepositoryTest {
    private final SurrealDbClient client = mock(SurrealDbClient.class);
    private final SurrealDbCargaMasivaParametrosRepository repository = new SurrealDbCargaMasivaParametrosRepository(client);

    @Test
    void debeGuardarElLoteEnUnaTransaccionConTodosLosRegistros() {
        var modulo = UUID.randomUUID();
        var funcionalidad = FuncionalidadEntity.create(UUID.randomUUID(), "Funcion'", modulo, true,
                OffsetDateTime.parse("2026-01-01T00:00:00Z"), null);
        var parametro = ParametroEntity.create(UUID.randomUUID(), "Parametro", funcionalidad.getId(), UUID.randomUUID(), true);
        var ambiente = UUID.randomUUID();
        var lote = new CargaMasivaLote(List.of(funcionalidad), List.of(parametro),
                List.of(MetadatoEntity.create(UUID.randomUUID(), parametro.getId(), UUID.randomUUID(), ambiente,
                        new ObjectMapper().createObjectNode().put("clave", "valor"))),
                List.of(MetadatoAmbienteEntity.create(UUID.randomUUID(), parametro.getId(), ambiente, UUID.randomUUID())));

        repository.guardar(lote);

        verify(client).execute(argThat(query -> query.startsWith("BEGIN TRANSACTION;")
                && query.contains("COMMIT TRANSACTION;")
                && query.contains("CREATE type::record('funcionalidades'")
                && query.contains("Funcion\\'")
                && query.contains("CREATE type::record('parametros'")
                && query.contains("CREATE type::record('metadatos'")
                && query.contains("CREATE type::record('metadatos_ambiente'")));
    }
}
