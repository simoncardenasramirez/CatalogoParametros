package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity;

import java.util.List;

/** Vista de administracion: solo datos de una aplicacion y catalogos auxiliares compartidos. */
public record CatalogoAplicacionEntity(AplicacionEntity aplicacion, List<ModuloEntity> modulos,
        List<FuncionalidadEntity> funcionalidades, List<ParametroEntity> parametros, List<MetadatoEntity> metadatos,
        List<MetadatoAmbienteEntity> metadatosAmbiente, List<AmbienteEntity> ambientes,
        List<TipoParametroEntity> tiposParametro, List<TipoMetadatoEntity> tiposMetadato,
        List<EstadoMetadatoAmbienteEntity> estadosMetadatoAmbiente) {
    public CatalogoAplicacionEntity {
        modulos = List.copyOf(modulos);
        funcionalidades = List.copyOf(funcionalidades);
        parametros = List.copyOf(parametros);
        metadatos = List.copyOf(metadatos);
        metadatosAmbiente = List.copyOf(metadatosAmbiente);
        ambientes = List.copyOf(ambientes);
        tiposParametro = List.copyOf(tiposParametro);
        tiposMetadato = List.copyOf(tiposMetadato);
        estadosMetadatoAmbiente = List.copyOf(estadosMetadatoAmbiente);
    }
}
