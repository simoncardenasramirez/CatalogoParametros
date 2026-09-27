package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response;

import java.util.ArrayList;
import java.util.List;

public final class CatalogResponse<T> extends Response {
    private final List<T> datos = new ArrayList<>();
    public List<T> getDatos() { return datos; }
}
