package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.catalogoaplicacion;

import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.CatalogoAplicacionEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.Response;
public final class CatalogoAplicacionResponse extends Response {
    private final CatalogoAplicacionEntity catalogo;
    public CatalogoAplicacionResponse(final CatalogoAplicacionEntity catalogo) { this.catalogo = catalogo; }
    public CatalogoAplicacionEntity getCatalogo() { return catalogo; }
}
