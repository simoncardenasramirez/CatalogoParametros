package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.funcionalidad;

import java.util.ArrayList;
import java.util.List;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.FuncionalidadEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.Response;

public final class FuncionalidadResponse extends Response {

    private final List<FuncionalidadEntity> funcionalidades = new ArrayList<>();

    public List<FuncionalidadEntity> getFuncionalidades() {
        return funcionalidades;
    }
}
