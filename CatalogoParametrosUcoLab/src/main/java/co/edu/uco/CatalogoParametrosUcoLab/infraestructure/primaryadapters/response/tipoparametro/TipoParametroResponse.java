package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.tipoparametro;

import java.util.ArrayList;
import java.util.List;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.Response;

public final class TipoParametroResponse extends Response {

    private final List<TipoParametroEntity> tiposParametro = new ArrayList<>();

    public List<TipoParametroEntity> getTiposParametro() {
        return tiposParametro;
    }
}
