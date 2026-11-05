package co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain;

import java.util.List;

public record CargaMasivaResultado(boolean ready, int totalFilas, int filasValidas, int filasInvalidas,
        List<CargaMasivaError> errores) {
    public CargaMasivaResultado {
        errores = List.copyOf(errores == null ? List.of() : errores);
    }
}
