package co.edu.uco.CatalogoParametrosUcoLab.application.features.catalogoaplicacion.consultarcatalogoaplicacion;

import java.util.UUID;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.CatalogoAplicacionEntity;
public interface ConsultarCatalogoAplicacion { CatalogoAplicacionEntity execute(UUID idAplicacion); }
