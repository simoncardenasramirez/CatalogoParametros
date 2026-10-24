package co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.administrarcatalogoaplicacion.primaryports.dto;

import tools.jackson.databind.JsonNode;

/** Union de campos de las operaciones existentes; no acepta idAplicacion desde el cuerpo. */
public record AdministrarCatalogoAplicacionDtoRequest(String nombre, String idModulo, String idFuncionalidad,
        String idTipoParametro, String idParametro, String idTipoMetadato, String idAmbiente,
        String idEstadoMetadatoAmbiente, Boolean activo, String fechaInicio, String fechaFinal, JsonNode valor) {
    public AdministrarCatalogoAplicacionDtoRequest {
        valor = valor == null ? null : valor.deepCopy();
    }
}
