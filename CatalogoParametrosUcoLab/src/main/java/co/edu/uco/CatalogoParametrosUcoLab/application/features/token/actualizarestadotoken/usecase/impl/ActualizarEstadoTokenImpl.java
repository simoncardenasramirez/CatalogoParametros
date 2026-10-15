package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.actualizarestadotoken.usecase.impl;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.actualizarestadotoken.ActualizarEstadoToken;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.actualizarestadotoken.usecase.domain.ActualizarEstadoTokenDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TokenEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoTokenRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TokenRepository;
import co.edu.uco.CatalogoParametrosUcoLab.crosscutting.exceptions.NotFoundException;

@Service
public final class ActualizarEstadoTokenImpl implements ActualizarEstadoToken {
    private final TokenRepository tokens;
    private final EstadoTokenRepository estados;

    public ActualizarEstadoTokenImpl(final TokenRepository tokens, final EstadoTokenRepository estados) {
        this.tokens = tokens;
        this.estados = estados;
    }

    @Override
    public TokenEntity execute(final ActualizarEstadoTokenDomain data) {
        if (tokens.findById(data.getId()).isEmpty()) throw NotFoundException.build("El token no existe.");
        if (estados.findById(data.getEstadoToken()).isEmpty()) {
            throw NotFoundException.build("El estado de token no existe.");
        }
        return tokens.updateEstado(data.getId(), data.getEstadoToken());
    }
}
