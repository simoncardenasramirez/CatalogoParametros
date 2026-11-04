package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.repository.cargamasiva;

import org.springframework.stereotype.Repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain.CargaMasivaLote;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.CargaMasivaParametrosRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.DateTimeHelper;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.secondaryadapters.surrealdb.SurrealDbClient;

@Repository
public final class SurrealDbCargaMasivaParametrosRepository implements CargaMasivaParametrosRepository {
    private final SurrealDbClient client;

    public SurrealDbCargaMasivaParametrosRepository(final SurrealDbClient client) {
        this.client = client;
    }

    @Override
    public void guardar(final CargaMasivaLote lote) {
        var query = new StringBuilder("BEGIN TRANSACTION;\n");
        lote.funcionalidades().forEach(entity -> query.append(funcionalidad(entity)));
        lote.parametros().forEach(entity -> query.append(parametro(entity)));
        lote.metadatos().forEach(entity -> query.append(metadato(entity)));
        lote.metadatosAmbiente().forEach(entity -> query.append(metadatoAmbiente(entity)));
        query.append("COMMIT TRANSACTION;");
        client.execute(query.toString());
    }

    private String funcionalidad(final FuncionalidadEntity entity) {
        return "CREATE type::record('funcionalidades', '%s') CONTENT { nombre: '%s', idModulo: '%s', activo: %s, fechaInicio: %s, fechaFinal: %s };\n"
                .formatted(entity.getId(), escape(entity.getNombre()), entity.getIdModulo(), entity.isActivo(),
                        DateTimeHelper.format(entity.getFechaInicio()), DateTimeHelper.format(entity.getFechaFinal()));
    }

    private String parametro(final ParametroEntity entity) {
        return "CREATE type::record('parametros', '%s') CONTENT { nombre: '%s', idFuncionalidad: '%s', idTipoParametro: '%s', activo: %s };\n"
                .formatted(entity.getId(), escape(entity.getNombre()), entity.getIdFuncionalidad(), entity.getIdTipoParametro(), entity.isActivo());
    }

    private String metadato(final MetadatoEntity entity) {
        return "CREATE type::record('metadatos', '%s') CONTENT { idParametro: '%s', idTipoMetadato: '%s', idAmbiente: '%s', valor: %s };\n"
                .formatted(entity.getId(), entity.getIdParametro(), entity.getIdTipoMetadato(), entity.getIdAmbiente(),
                        entity.getValor() == null ? "null" : entity.getValor().toString());
    }

    private String metadatoAmbiente(final MetadatoAmbienteEntity entity) {
        return "CREATE type::record('metadatos_ambiente', '%s') CONTENT { idParametro: '%s', idAmbiente: '%s', idEstadoMetadatoAmbiente: '%s' };\n"
                .formatted(entity.getId(), entity.getIdParametro(), entity.getIdAmbiente(), entity.getIdEstadoMetadatoAmbiente());
    }

    private String escape(final String value) {
        return value == null ? "" : value.replace("\\", "\\\\").replace("'", "\\'");
    }
}
