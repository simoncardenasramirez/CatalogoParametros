package co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain;

public record CargaMasivaError(int fila, String hoja, String campo, String valor, String codigo,
        String descripcion) {
}
