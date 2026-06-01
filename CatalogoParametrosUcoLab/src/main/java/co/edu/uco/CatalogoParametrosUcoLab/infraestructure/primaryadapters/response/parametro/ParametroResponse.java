package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.parametro;

import java.util.ArrayList;
import java.util.List;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ParametroEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.Response;

public final class ParametroResponse extends Response {

    private final List<ParametroEntity> parametros = new ArrayList<>();

    public List<ParametroEntity> getParametros() {
        return parametros;
    }
}
