package co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain;

import tools.jackson.databind.JsonNode;

public record CargaMasivaFila(int fila, String organizacion, String aplicacion, String modulo, String funcionalidad,
        String fechaInicioFuncionalidad, String fechaFinalFuncionalidad, String activoFuncionalidad,
        String parametro, String tipoParametro, String activoParametro, String tipoMetadato,
        String ambiente, String estadoMetadatoAmbiente, JsonNode valor) {
}
