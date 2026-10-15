package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.creartoken;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.creartoken.usecase.domain.CrearTokenDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TokenEntity;

public interface CrearToken {
    TokenEntity execute(CrearTokenDomain data);
}
