package co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.cargamasiva.parametros.usecase.domain.CargaMasivaLote;

public interface CargaMasivaParametrosRepository {
    void guardar(CargaMasivaLote lote);
}
