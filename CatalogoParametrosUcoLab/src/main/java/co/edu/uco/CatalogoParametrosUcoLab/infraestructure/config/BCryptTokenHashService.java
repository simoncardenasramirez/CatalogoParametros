package co.edu.uco.CatalogoParametrosUcoLab.infraestructure.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.common.TokenHashService;

@Component
public final class BCryptTokenHashService implements TokenHashService {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String hash(final String token) {
        return encoder.encode(token);
    }

    @Override
    public boolean matches(final String token, final String tokenHash) {
        return token != null && tokenHash != null && encoder.matches(token, tokenHash);
    }
}
