package co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain;

import java.util.List;

public record CargaMasivaLibro(List<CargaMasivaFila> filas, List<CargaMasivaError> errores) {
    public CargaMasivaLibro {
        filas = List.copyOf(filas == null ? List.of() : filas);
        errores = List.copyOf(errores == null ? List.of() : errores);
    }
}
