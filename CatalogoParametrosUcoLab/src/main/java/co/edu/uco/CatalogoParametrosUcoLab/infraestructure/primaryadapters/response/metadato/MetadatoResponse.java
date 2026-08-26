package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.metadato;

import java.util.ArrayList;
import java.util.List;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.MetadatoEntity;
import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.Response;
public final class MetadatoResponse extends Response {
    private final List<MetadatoEntity> metadatos = new ArrayList<>();
    public List<MetadatoEntity> getMetadatos() { return metadatos; }
}
