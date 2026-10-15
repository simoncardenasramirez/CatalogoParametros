package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosportoken;

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

public interface ConsultarParametrosPorToken {
    Resultado execute(UUID tokenId, String tokenPlano);

    record Resultado(boolean autorizado, AplicacionEntity aplicacion, AmbienteEntity ambiente,
            List<ParametroDetalle> parametros) {
        public static Resultado noAutorizado() { return new Resultado(false, null, null, List.of()); }
        public static Resultado autorizado(final AplicacionEntity aplicacion, final AmbienteEntity ambiente,
                final List<ParametroDetalle> parametros) {
            return new Resultado(true, aplicacion, ambiente, List.copyOf(parametros));
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
