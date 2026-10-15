package co.edu.uco.CatalogoParametrosUcoLab.application.features.token.validartoken.usecase.impl;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.common.TokenHashService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.validartoken.ValidarToken;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.validartoken.usecase.domain.ValidarTokenDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoTokenRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TokenRepository;

@Service
public final class ValidarTokenImpl implements ValidarToken {
    private final TokenRepository tokens;
    private final EstadoTokenRepository estados;
    private final TokenHashService hashService;

    public ValidarTokenImpl(final TokenRepository tokens, final EstadoTokenRepository estados,
            final TokenHashService hashService) {
        this.tokens = tokens;
        this.estados = estados;
        this.hashService = hashService;
    }

    @Override
    public boolean execute(final ValidarTokenDomain data) {
        return tokens.findById(data.getId()).filter(token -> token.getFechaExpiracion() != null
                        && token.getFechaExpiracion().isAfter(LocalDateTime.now()))
                .filter(token -> estados.findById(token.getEstadoToken())
                        .map(estado -> "activo".equalsIgnoreCase(estado.getEstado())).orElse(false))
                .filter(token -> hashService.matches(data.getToken(), token.getTokenHash()))
                .isPresent();
    }
}
