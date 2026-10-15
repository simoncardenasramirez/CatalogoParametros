package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosportoken.primaryports.interactor.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosportoken.ConsultarParametrosPorToken;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosportoken.ConsultarParametrosPorToken.Resultado;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.consultarparametrosportoken.primaryports.interactor.ConsultarParametrosPorTokenInteractor;

@Service
public final class ConsultarParametrosPorTokenInteractorImpl implements ConsultarParametrosPorTokenInteractor {
    private static final String BEARER = "Bearer ";
    private final ConsultarParametrosPorToken consultar;

    public ConsultarParametrosPorTokenInteractorImpl(final ConsultarParametrosPorToken consultar) {
        this.consultar = consultar;
    }

    @Override
    public Resultado execute(final UUID tokenId, final String authorization) {
        if (authorization == null || !authorization.startsWith(BEARER)) return Resultado.noAutorizado();
        var tokenPlano = authorization.substring(BEARER.length()).trim();
        if (tokenPlano.isEmpty()) return Resultado.noAutorizado();
        return consultar.execute(tokenId, tokenPlano);
    }
}
