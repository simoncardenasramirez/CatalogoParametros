package co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain.CargaMasivaResultado;

public interface CargarCatalogoMasivo {
    byte[] plantilla();
    CargaMasivaResultado validar(byte[] archivo);
    CargaMasivaResultado cargar(byte[] archivo);
    byte[] reporteErrores(byte[] archivo);
}
