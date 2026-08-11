package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.modulo;

import java.util.ArrayList;
import java.util.List;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.ModuloEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.Response;

public final class ModuloResponse extends Response {

    private final List<ModuloEntity> modulos = new ArrayList<>();

    public List<ModuloEntity> getModulos() {
        return modulos;
    }
}
