package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.validartoken.usecase.domain;

import java.util.UUID;

import co.edu.uco.CatalogoParametrosUcoLab.application.usecase.domain.Domain;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.helpers.TextHelper;

public final class ValidarTokenDomain extends Domain {
    private final String token;

    private ValidarTokenDomain(final UUID id, final String token) {
        super(id);
        this.token = TextHelper.applyTrim(token);
    }

    public static ValidarTokenDomain create(final UUID id, final String token) {
        return new ValidarTokenDomain(id, token);
    }

    public String getToken() { return token; }
}
