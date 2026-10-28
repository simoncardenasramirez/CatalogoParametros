package co.edu.uco.CatalogoParametrosUcoLab.application.features.token;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import co.edu.uco.CatalogoParametrosUcoLab.application.primaryports.ControlEstadoCatalogo;
import co.edu.uco.CatalogoParametrosUcoLab.application.secondaryports.entity.TipoRegistroVigencia;
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
    @Mock private ControlEstadoCatalogo controlEstado;
    private ValidarTokenImpl useCase;
    private UUID tokenId;
    private UUID estadoId;

    @BeforeEach
    void preparar() {
        useCase = new ValidarTokenImpl(tokens, estados, hashService, controlEstado, java.time.Clock.systemUTC());
        tokenId = UUID.randomUUID();
        estadoId = UUID.randomUUID();
    }

    @Test
    void debeValidarTokenActivoVigenteYCoincidente() {
        var token = token(OffsetDateTime.now(java.time.ZoneOffset.UTC).plusHours(1));
        when(tokens.findById(tokenId)).thenReturn(Optional.of(token));
        when(estados.findById(estadoId)).thenReturn(Optional.of(EstadoTokenEntity.create(estadoId, "activo")));
        when(hashService.matches("secreto", "hash")).thenReturn(true);

        when(controlEstado.disponible(TipoRegistroVigencia.TOKEN, tokenId)).thenReturn(true);
        assertTrue(useCase.execute(ValidarTokenDomain.create(tokenId, "secreto")));
        verify(hashService).matches("secreto", "hash");
    }

    @Test
    void debeRechazarTokenExpirado() {
        when(tokens.findById(tokenId)).thenReturn(Optional.of(token(OffsetDateTime.now(java.time.ZoneOffset.UTC).minusSeconds(1))));

        assertFalse(useCase.execute(ValidarTokenDomain.create(tokenId, "secreto")));
    }

    @Test
    void debeRechazarTokenRevocado() {
        when(tokens.findById(tokenId)).thenReturn(Optional.of(token(OffsetDateTime.now(java.time.ZoneOffset.UTC).plusHours(1))));
        when(estados.findById(estadoId)).thenReturn(Optional.of(EstadoTokenEntity.create(estadoId, "revocado")));

        assertFalse(useCase.execute(ValidarTokenDomain.create(tokenId, "secreto")));
    }

    private TokenEntity token(final OffsetDateTime expiracion) {
        return TokenEntity.create(tokenId, "hash", estadoId, UUID.randomUUID(), UUID.randomUUID(),
                OffsetDateTime.now(java.time.ZoneOffset.UTC), expiracion);
    }
}
