package co.edu.uco.CatalogoParametrosUcoLab.application.features.cambiarestado.primaryports.interactor;

import java.util.UUID;

public interface CambiarEstadoInteractor {

    enum TipoRecurso {
        APLICACION,
        MODULO,
        FUNCIONALIDAD,
        PARAMETRO
    }

    void execute(TipoRecurso tipoRecurso, UUID id, Boolean activo);
}
