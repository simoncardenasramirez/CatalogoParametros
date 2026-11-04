package co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain;

import java.util.List;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoAmbienteEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;

public record CargaMasivaLote(List<FuncionalidadEntity> funcionalidades, List<ParametroEntity> parametros,
        List<MetadatoEntity> metadatos, List<MetadatoAmbienteEntity> metadatosAmbiente) {
    public CargaMasivaLote {
        funcionalidades = List.copyOf(funcionalidades == null ? List.of() : funcionalidades);
        parametros = List.copyOf(parametros == null ? List.of() : parametros);
        metadatos = List.copyOf(metadatos == null ? List.of() : metadatos);
        metadatosAmbiente = List.copyOf(metadatosAmbiente == null ? List.of() : metadatosAmbiente);
    }
}
