package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.validartoken;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.validartoken.usecase.domain.ValidarTokenDomain;

public interface ValidarToken {
    boolean execute(ValidarTokenDomain data);
}
