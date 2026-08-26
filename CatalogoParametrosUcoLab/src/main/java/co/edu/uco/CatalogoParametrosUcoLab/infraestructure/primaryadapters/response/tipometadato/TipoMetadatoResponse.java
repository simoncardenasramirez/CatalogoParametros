package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.tipometadato;

import java.util.ArrayList;
import java.util.List;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoMetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.Response;

public final class TipoMetadatoResponse extends Response {
    private final List<TipoMetadatoEntity> tiposMetadato = new ArrayList<>();
    public List<TipoMetadatoEntity> getTiposMetadato() { return tiposMetadato; }
}
