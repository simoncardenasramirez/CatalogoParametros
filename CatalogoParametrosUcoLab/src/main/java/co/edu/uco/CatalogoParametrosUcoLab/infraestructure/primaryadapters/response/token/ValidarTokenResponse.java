package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.token;

import co.edu.uco.CatalogoParametrosUcoLab.infraestructure.primaryadapters.response.Response;

public final class ValidarTokenResponse extends Response {
    private final boolean valido;
    public ValidarTokenResponse(final boolean valido) { this.valido = valido; }
    public boolean isValido() { return valido; }
}
