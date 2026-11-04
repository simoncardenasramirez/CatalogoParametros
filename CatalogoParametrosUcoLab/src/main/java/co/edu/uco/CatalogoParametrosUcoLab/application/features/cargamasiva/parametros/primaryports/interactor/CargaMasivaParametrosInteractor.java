package co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.primaryports.interactor;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain.CargaMasivaResultado;

public interface CargaMasivaParametrosInteractor {
    byte[] plantilla();
    CargaMasivaResultado validar(byte[] archivo);
    CargaMasivaResultado cargar(byte[] archivo);
    byte[] reporteErrores(byte[] archivo);
}
