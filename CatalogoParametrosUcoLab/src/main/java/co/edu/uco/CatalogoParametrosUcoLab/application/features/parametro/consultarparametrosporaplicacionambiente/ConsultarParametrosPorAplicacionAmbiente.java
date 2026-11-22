package co.edu.uco.CatalogoParametrosUcoLab.application.features.parametro.consultarparametrosporaplicacionambiente;

import java.util.List;
import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.AplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoMetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoMetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoParametroEntity;

/** Consulta los parámetros disponibles para una aplicación y un ambiente autorizados. */
public interface ConsultarParametrosPorAplicacionAmbiente {
    Resultado execute(UUID aplicacionId, UUID ambienteId);

    record Resultado(AplicacionEntity aplicacion, AmbienteEntity ambiente,
            List<ParametroDetalle> parametros) {
        public Resultado {
            parametros = List.copyOf(parametros);
        }
    }

    record ParametroDetalle(ParametroEntity parametro, TipoParametroEntity tipoParametro,
            FuncionalidadEntity funcionalidad, ModuloEntity modulo, List<MetadatoDetalle> metadatos,
            MetadatoAmbienteDetalle configuracionAmbiente) {
        public ParametroDetalle {
            metadatos = List.copyOf(metadatos);
        }
    }

    record MetadatoDetalle(MetadatoEntity metadato, TipoMetadatoEntity tipoMetadato) { }

    record MetadatoAmbienteDetalle(MetadatoAmbienteEntity asociacion,
            EstadoMetadatoAmbienteEntity estado) { }
}
