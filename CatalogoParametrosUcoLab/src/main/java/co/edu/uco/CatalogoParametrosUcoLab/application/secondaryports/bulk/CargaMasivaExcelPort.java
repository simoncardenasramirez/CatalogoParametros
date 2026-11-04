package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.bulk;

import java.util.List;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain.CargaMasivaError;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain.CargaMasivaLibro;

public interface CargaMasivaExcelPort {
    byte[] plantilla();
    CargaMasivaLibro leer(byte[] archivo);
    byte[] anotar(byte[] archivo, List<CargaMasivaError> errores);
}
