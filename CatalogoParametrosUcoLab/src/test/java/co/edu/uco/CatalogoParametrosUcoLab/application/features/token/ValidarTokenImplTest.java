package co.edu.uco.CatalogoParametrosUcoLab.application.features.token;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.common.TokenHashService;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.validartoken.usecase.domain.ValidarTokenDomain;
import co.edu.uco.CatalogoParametrosUcoLab.application.features.token.validartoken.usecase.impl.ValidarTokenImpl;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.EstadoTokenEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TokenEntity;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.EstadoTokenRepository;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.repository.TokenRepository;

@ExtendWith(MockitoExtension.class)
class ValidarTokenImplTest {
    @Mock private TokenRepository tokens;
    @Mock private EstadoTokenRepository estados;
    @Mock private TokenHashService hashService;
    private ValidarTokenImpl useCase;
    private UUID tokenId;
    private UUID estadoId;

    @BeforeEach
    void preparar() {
        useCase = new ValidarTokenImpl(tokens, estados, hashService);
        tokenId = UUID.randomUUID();
        estadoId = UUID.randomUUID();
    }

    @Test
    void debeValidarTokenActivoVigenteYCoincidente() {
        var token = token(LocalDateTime.now().plusHours(1));
        when(tokens.findById(tokenId)).thenReturn(Optional.of(token));
        when(estados.findById(estadoId)).thenReturn(Optional.of(EstadoTokenEntity.create(estadoId, "activo")));
        when(hashService.matches("secreto", "hash")).thenReturn(true);

        assertTrue(useCase.execute(ValidarTokenDomain.create(tokenId, "secreto")));
        verify(hashService).matches("secreto", "hash");
    }

    @Test
    void debeRechazarTokenExpirado() {
        when(tokens.findById(tokenId)).thenReturn(Optional.of(token(LocalDateTime.now().minusSeconds(1))));

        assertFalse(useCase.execute(ValidarTokenDomain.create(tokenId, "secreto")));
    }

    @Test
    void debeRechazarTokenRevocado() {
        when(tokens.findById(tokenId)).thenReturn(Optional.of(token(LocalDateTime.now().plusHours(1))));
        when(estados.findById(estadoId)).thenReturn(Optional.of(EstadoTokenEntity.create(estadoId, "revocado")));

        assertFalse(useCase.execute(ValidarTokenDomain.create(tokenId, "secreto")));
    }

    private TokenEntity token(final LocalDateTime expiracion) {
        return TokenEntity.create(tokenId, "hash", estadoId, UUID.randomUUID(), UUID.randomUUID(),
                LocalDateTime.now(), expiracion);
    }
}
