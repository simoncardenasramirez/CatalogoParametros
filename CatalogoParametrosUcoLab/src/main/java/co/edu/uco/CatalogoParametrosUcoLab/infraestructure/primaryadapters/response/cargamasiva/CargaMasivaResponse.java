package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.cargamasiva;

import java.util.ArrayList;
import java.util.List;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain.CargaMasivaError;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain.CargaMasivaResultado;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.Response;

public final class CargaMasivaResponse extends Response {
    private final boolean ready;
    private final int totalFilas;
    private final int filasValidas;
    private final int filasInvalidas;
    private final List<CargaMasivaError> errores;

    private CargaMasivaResponse(final CargaMasivaResultado resultado) {
        ready = resultado.ready();
        totalFilas = resultado.totalFilas();
        filasValidas = resultado.filasValidas();
        filasInvalidas = resultado.filasInvalidas();
        errores = new ArrayList<>(resultado.errores());
        getMensajes().add(resultado.ready()
                ? "El archivo esta listo para cargar."
                : "El archivo contiene errores y no puede cargarse.");
    }

    public static CargaMasivaResponse from(final CargaMasivaResultado resultado) {
        return new CargaMasivaResponse(resultado);
    }

    public boolean isReady() { return ready; }
    public int getTotalFilas() { return totalFilas; }
    public int getFilasValidas() { return filasValidas; }
    public int getFilasInvalidas() { return filasInvalidas; }
    public List<CargaMasivaError> getErrores() { return errores; }
}
