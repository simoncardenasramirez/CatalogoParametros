package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.actualizarestadotoken;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.actualizarestadotoken.usecase.domain.ActualizarEstadoTokenDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TokenEntity;

public interface ActualizarEstadoToken {
    TokenEntity execute(ActualizarEstadoTokenDomain data);
}
